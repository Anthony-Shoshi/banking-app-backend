package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/{userId}/account-detail")
    //@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    public ResponseEntity<Object> getAccountDetails(@PathVariable Long userId){
        return ResponseEntity.status(200).body(accountService.getAccountDetails(userId));
    }
}
