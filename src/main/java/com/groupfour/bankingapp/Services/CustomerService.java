package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Models.DTO.CustomerGetWithOutAccountDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.exception.InvalidLimitException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    public CustomerService(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService= accountService;
    }

    public List<CustomerGetWithOutAccountDTO> getCustomersWithoutAccounts() {
        return customerRepository.findCustomersWithoutAccounts().stream()
                .filter(customer -> customer.getUser().getUserId() != null)
                .map(customer -> new CustomerGetWithOutAccountDTO(
                        customer.getUser().getUserId(),
                        customer.getUser().getFirstName()+" "+customer.getUser().getLastName(),
                        customer.getStatus())

                )
                .collect(Collectors.toList());
    }



    public void approveSignup(long customerId, ApproveSignupPutDTO approveSignupPutDTO) throws EntityNotFoundException, InvalidLimitException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this id: " + customerId));

        if (approveSignupPutDTO.dailyLimit() < 0 || approveSignupPutDTO.absoluteLimitForCurrent() < 0 || approveSignupPutDTO.absoluteLimitForSaving() < 0) {
            throw new InvalidLimitException("Limits cannot be negative");
        }
        try {
            accountService.createAccount(customer, approveSignupPutDTO);
            customer.setStatus(CustomerStatus.APPROVED);
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Account creation failed: " + e.getMessage(), e);
        }
    }
}
