package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
//@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest request;

    public TransactionController(TransactionService transactionService, JwtTokenProvider jwtTokenProvider, HttpServletRequest request, UserService userService) {
        this.transactionService = transactionService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.request = request;
        this.userService = userService;
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

        try {
            return ResponseEntity.status(200).body(transactionService.getAllTransactions());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<BankTransactionDTO>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        // Log incoming request for debugging
        System.out.println("Fetching transactions for customer ID: " + customerId);

        Optional<List<BankTransactionDTO>> transactions = transactionService.getTransactionsByCustomerId(customerId);
        if (transactions.isPresent()) {
            return ResponseEntity.ok(transactions.get());
        } else {
            return ResponseEntity.status(404).build();
        }
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

        List<BankTransactionDTO> transactions = transactionService.getTransactionHistory(customerId, start, end, fromAmount, toAmount, iban);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transactions")
    public ResponseEntity<BankTransactionPostDTO> transferMoney(@RequestBody BankTransactionPostDTO transferRequest, HttpServletRequest request) {
        try {
            // Log headers
            System.out.println("Request Headers:");
            request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                    System.out.println(headerName + ": " + request.getHeader(headerName))
            );

            // Log request body
            System.out.println("Request Body:");
            System.out.println(transferRequest);

            String token = jwtTokenProvider.resolveToken(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                System.err.println("Token is invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            User initiator = userService.getCurrentLoggedInUser(request);
            if (initiator == null) {
                System.err.println("Failed to create transaction: Initiator is null");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            BankTransactionPostDTO transactionDTO = transactionService.transferMoney(
                    transferRequest.fromAccountIban(),
                    transferRequest.toAccountIban(),
                    transferRequest.transferAmount()
            );
            return ResponseEntity.status(HttpStatus.OK).body(transactionDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
