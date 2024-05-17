package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<BankTransactionDTO> getAllTransactions() {
        List<BankTransaction> transactions = transactionRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return transactions.stream().map(transaction -> {
            String formattedDateTime = transaction.getCurrentTime().format(formatter);
            return new BankTransactionDTO(
                    transaction.getType(),
                    transaction.getUser().getRole(),
                    transaction.getUser().getFirstName(),
                    transaction.getUser().getLastName(),
                    transaction.getFromAccount().getIBAN(),
                    transaction.getToAccount().getIBAN(),
                    transaction.getTransferAmount(),
                    formattedDateTime,
                    transaction.getStatus()
            );
        }).collect(Collectors.toList());
    }


}
