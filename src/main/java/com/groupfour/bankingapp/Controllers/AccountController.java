package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping("/employees/customer-accounts")
    //@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> getAllAccounts(){
        return  ResponseEntity.status(200).body(accountService.getAllAccountDetails());
    }

//    @GetMapping("/account-detail")
//    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
//    public ResponseEntity<Object> getAccountDetails(@PathVariable Long userId){
//        return ResponseEntity.status(200).body(accountService.getAccountDetails(userId));
//    }

    @GetMapping("/{userId}/account-detail")
    public ResponseEntity<Object> getAccountDetails(@PathVariable Long userId) {
        try {
            Object accountDetails = accountService.getAccountDetails(userId);
            return ResponseEntity.ok(accountDetails);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
