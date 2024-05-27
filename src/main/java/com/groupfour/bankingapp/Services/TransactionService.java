package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public Optional<List<BankTransactionDTO>> getTransactionsByCustomerId(Long customerId) {
        List<BankTransaction> transactions = transactionRepository.findByFromAccountCustomerCustomerId(customerId);
        if (transactions.isEmpty()) {
            return Optional.empty();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<BankTransactionDTO> transactionDTOs = transactions.stream().map(transaction -> {
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

        return Optional.of(transactionDTOs);
    }

    public List<BankTransactionDTO> getTransactionHistory(Long customerId, LocalDate startDate, LocalDate endDate, Double fromAmount, Double toAmount, String iban) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

        List<BankTransaction> transactions;

        if (customerId == null && startDate == null && endDate == null && fromAmount == null && toAmount == null && (iban == null || iban.isEmpty())) {
            transactions = transactionRepository.findAll();
        } else {
            transactions = transactionRepository.findFilteredTransactions(customerId, startDateTime, endDateTime, fromAmount, toAmount, iban);
        }

        return transactions.stream()
                .map(transaction -> new BankTransactionDTO(
                        transaction.getType(),
                        transaction.getUser().getRole(),
                        transaction.getUser().getFirstName(),
                        transaction.getUser().getLastName(),
                        transaction.getFromAccount().getIBAN(),
                        transaction.getToAccount().getIBAN(),
                        transaction.getTransferAmount(),
                        transaction.getCurrentTime().toLocalDate().toString(),
                        transaction.getStatus()))
                .collect(Collectors.toList());
    }
}
