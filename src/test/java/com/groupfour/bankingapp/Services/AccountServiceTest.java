package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Account;
import com.groupfour.bankingapp.Models.AccountType;
import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Before("")
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllAccountDetails() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setCustomer(new Customer()); // Set all required fields similarly
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account));

        List<AccountsGetDTO> results = accountService.getAllAccountDetails();
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(Long.valueOf(1), results.get(0).accountId());
    }
    @Test
    public void testCreateAccount() {
        Customer customer = new Customer();
        customer.setCustomerId(1L); // setup customer with necessary fields
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(1000.0, 500.0, 1500.0);

        Account account = new Account();
        account.setAccountType(AccountType.CURRENT);

        doAnswer(invocation -> {
            Account ac = invocation.getArgument(0);
            ac.setAccountId(1L); // simulate setting ID after save
            return null;
        }).when(accountRepository).save(any(Account.class));

        accountService.createAccount(customer, dto);

        verify(accountRepository, times(2)).save(any(Account.class)); // Called twice for current and saving
    }

    @Test
    public void testGenerateUniqueIBAN() {
        when(accountRepository.existsByIBAN(anyString())).thenReturn(false);
        String iban = accountService.generateUniqueIBAN();
        assertNotNull(iban);
        assertTrue(iban.startsWith("NL"));
    }
}
