package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Services.TransactionService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@ControllerAdvice
//@CrossOrigin("*")
@Log
@RequestMapping("/transactions")
//@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin to match your frontend URL
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private static ResponseEntity<Object> createTransactionNotFoundResponse(long id) {
        String message = "Transaction with id %d not found".formatted(id);
        Map<String, String> response = Collections.singletonMap("message", message);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @GetMapping//("/transactions")
    public ResponseEntity<List<BankTransaction>> getAllTransactions() {
        List<BankTransaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transactions);
        }
    }


}
