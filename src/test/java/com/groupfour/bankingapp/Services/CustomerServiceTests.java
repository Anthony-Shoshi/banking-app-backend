package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Models.DTO.CustomerGetWithOutAccountDTO;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.exception.InvalidLimitException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setStatus(CustomerStatus.PENDING);
        when(customerRepository.findCustomersWithoutAccounts()).thenReturn(Collections.singletonList(customer));


        lenient().when(customerRepository.findCustomersWithoutAccounts()).thenReturn(Collections.singletonList(customer));

    }

    @Test
    public void testGetCustomersWithoutAccounts() {

        List<CustomerGetWithOutAccountDTO> results = customerService.getCustomersWithoutAccounts();
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).name());
    }


    @Test
    public void testApproveSignupSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setStatus(CustomerStatus.PENDING);
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(1000.0, 500.0, 1500.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(accountService).createAccount(customer, dto);

        customerService.approveSignup(1L, dto);

        verify(customerRepository).save(customer);
        verify(accountService).createAccount(customer, dto);
        assertEquals(CustomerStatus.APPROVED, customer.getStatus(), "Customer status should be set to APPROVED");
    }


    @Test
    public void testApproveSignupNotFound() throws Exception {
        // Setup
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Action & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            customerService.approveSignup(1L, new ApproveSignupPutDTO(1000.0, 500.0, 1500.0));
        }, "Should throw EntityNotFoundException for non-existent customer ID");
    }

    @Test
    public void testApproveSignupInvalidLimit() throws InvalidLimitException {
        // Setup
        Customer customer = new Customer();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(-100.0, -500.0, -1500.0);

        // Action & Assert
        assertThrows(InvalidLimitException.class, () -> {
            customerService.approveSignup(1L, dto);
        }, "Should throw InvalidLimitException for negative limits");
    }

    @Test
    public void testApproveSignupAccountCreationFailure() throws Exception {
        // Setup
        Customer customer = new Customer();
        customer.setStatus(CustomerStatus.PENDING);
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(1000.0, 500.0, 1500.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doThrow(new RuntimeException("Account creation failed")).when(accountService).createAccount(customer, dto);

        // Action & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.approveSignup(1L, dto);
        }, "Should throw RuntimeException when account creation fails");

        // Verify that the customer status is not changed to APPROVED if account creation fails
        assertEquals(CustomerStatus.PENDING, customer.getStatus(), "Customer status should not be APPROVED after account creation fails");
        assertTrue(exception.getMessage().contains("Account creation failed"), "Exception message should indicate failure in account creation");
    }
}
