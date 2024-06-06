package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Models.DTO.CustomerGetWithOutAccountDTO;
import com.groupfour.bankingapp.Services.CustomerService;
import com.groupfour.bankingapp.exception.InvalidLimitException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/employees/customers-without-accounts")
    public ResponseEntity<Object> getCustomersWithoutAccounts() {
        List<CustomerGetWithOutAccountDTO> customers = customerService.getCustomersWithoutAccounts();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/employees/customers-without-accounts/{userId}/approve-signup")
    //@PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<Object> approveSignup(@PathVariable Long userId, @RequestBody ApproveSignupPutDTO approveSignupPutDTO){
        try {
            customerService.approveSignup(userId, approveSignupPutDTO);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidLimitException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
