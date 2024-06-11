package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.UpdateAbsoluteLimitDTO;
import com.groupfour.bankingapp.Models.DTO.UpdateAccountLimitsDTO;
import com.groupfour.bankingapp.Models.DTO.UpdateDailyLimitDTO;
import com.groupfour.bankingapp.Services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/customers/search-iban")
    public ResponseEntity<Object> getIbansByCustomerName(
            @RequestParam(name = "firstName") String firstName,
            @RequestParam(name = "lastName") String lastName) {
        try {
            String iban = accountService.getCurrentAccountIbanByCustomerName(firstName, lastName);
            Map<String, String> response = new HashMap<>();
            response.put("iban", iban);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PutMapping("/employees/customer-accounts")
    public ResponseEntity<String> updateAccountLimits(@RequestBody UpdateAccountLimitsDTO updateAccountLimitsDTO) {
        try {
            accountService.updateAccountLimits(updateAccountLimitsDTO);
            return ResponseEntity.ok("Account limits updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update account limits.");
        }
    }
    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<AccountsGetDTO>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountsGetDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

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
