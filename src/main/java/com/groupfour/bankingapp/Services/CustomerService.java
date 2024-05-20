package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Models.DTO.CustomerGetWithOutAccountDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
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
                .map(customer -> new CustomerGetWithOutAccountDTO(
                        customer.getUser().getUserId(),
                        customer.getUser().getFirstName()+" "+customer.getUser().getLastName(),
                        customer.getStatus())
                       // customer.getUser().getBirthDate(),
                      //  customer.getGender())
                )
                .collect(Collectors.toList());
    }



    public void approveSignup(long customerId, ApproveSignupPutDTO approveSignupPutDTO)throws EntityNotFoundException{
        Customer customer= customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found with this id:" + customerId));
        customer.setStatus(CustomerStatus.APPROVED);
        accountService.createAccount(customer, approveSignupPutDTO);
        customerRepository.save(customer);
    }
}
