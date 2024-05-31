package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Models.DTO.AccountPutDTO;
import com.groupfour.bankingapp.Services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/employees/customer-accounts")
    //@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> getAllAccounts(){
        return  ResponseEntity.status(200).body(accountService.getAllAccountDetails());
    }
    @PutMapping("/employees/update-daily-limit")
    public ResponseEntity<Object> updateDailyLimit(@RequestBody AccountPutDTO accountPutDTO) {
        try {
            accountService.updateDailyLimit(accountPutDTO.accountId(), accountPutDTO.dailyLimit());
            return ResponseEntity.status(200).body("Daily limit updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
