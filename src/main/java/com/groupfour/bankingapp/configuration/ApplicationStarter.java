package com.groupfour.bankingapp.configuration;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.TransactionDTO;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import com.groupfour.bankingapp.Services.TransactionService;
import jakarta.transaction.Transactional;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class ApplicationStarter implements ApplicationRunner {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;

    public ApplicationStarter(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository= transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
// Retrieve the User instance from the database based on the user ID
        User user = userRepository.findById(1234L).orElse(null);

// Check if the user exists before creating the Transaction
        if (user != null) {
            Transaction t = new Transaction(
                    TransactionType.DEPOSIT,
                    UserType.ROLE_CUSTOMER,
                    user, // Pass the User instance
                    5678L, // From Account ID
                    91011L, // To Account ID
                    1000.00, // Transfer Amount
                    LocalDateTime.now(),
                    TransactionStatus.SUCCESS
            );
            transactionRepository.save(t);
        } else {
            System.out.println("User not found with ID: 1234");
        }

        // Retrieve and print all transactions from the database
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        allTransactions.forEach(transaction -> System.out.println("Transaction ID: " + transaction.getId()));
    }
}


