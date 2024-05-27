package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Services.TransactionService;
import lombok.extern.java.Log;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@ControllerAdvice
@Log
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

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions() {

        try{
            return ResponseEntity.status(200).body(transactionService.getAllTransactions());
        }
        catch (Exception exception) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<BankTransactionDTO>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        Optional<List<BankTransactionDTO>> transactions = transactionService.getTransactionsByCustomerId(customerId);
        return transactions.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @GetMapping("/customers/transaction-history")
    public ResponseEntity<List<BankTransactionDTO>> getTransactionHistory(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
            @RequestParam(required = false) Double fromAmount,
            @RequestParam(required = false) Double toAmount,
            @RequestParam(required = false) String iban) {

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        System.out.println("CustomerId: " + customerId);
        System.out.println("StartDate: " + start);
        System.out.println("EndDate: " + end);

        List<BankTransactionDTO> transactions = transactionService.getTransactionHistory(customerId, start, end, fromAmount, toAmount, iban);
        return ResponseEntity.ok(transactions);
    }
}
