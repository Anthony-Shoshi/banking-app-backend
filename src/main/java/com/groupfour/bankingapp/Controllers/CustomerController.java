package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/employees/customers-without-accounts")
    public ResponseEntity<List<Customer>> getCustomersWithoutAccounts(){
        List<Customer> customers = customerService.getCustomersWithoutAccounts();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/employees/customers-without-accounts/{userId}/approve-signup")
   //@PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<Object> approveSignup(@PathVariable Long userId, @RequestBody ApproveSignupPutDTO approveSignupPutDTO){
        customerService.approveSignup(userId, approveSignupPutDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new Object[0]);
    }
}
