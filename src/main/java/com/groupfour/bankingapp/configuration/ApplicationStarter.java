package com.groupfour.bankingapp.configuration;

import com.groupfour.bankingapp.Models.Transaction;
import com.groupfour.bankingapp.Models.DTO.TransactionDTO;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
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
    public ApplicationStarter(TransactionService transactionService) {
        this.transactionService = transactionService;

    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<TransactionDTO> transactions = List.of(

                new TransactionDTO(1L,TransactionType.DEPOSIT,UserType.ROLE_CUSTOMER,
                1234L, // User ID
                5678L, // From Account ID
                91011L, // To Account ID
                1000.00, // Transfer Amount
                LocalDateTime.now(), TransactionStatus.SUCCESS)
        );

        transactionService.getAllTransactions().forEach(transaction -> System.out.println("hhhh"));

    }
}
