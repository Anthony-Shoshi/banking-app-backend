package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.TransactionRequestDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
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

//    @Transactional
//    public BankTransaction deposit(TransactionRequestDTO request) {
//        Account account = accountRepository.findById(request.getAccountId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
//
//        account.setBalance(account.getBalance() + request.getAmount());
//        accountRepository.save(account);
//
//        BankTransaction transaction = new BankTransaction(
//                TransactionType.DEPOSIT,
//                UserType.CUSTOMER,
//                account.getCustomer().getUser(),
//                account,
//                null,
//                request.getAmount(),
//                LocalDateTime.now(),
//                TransactionStatus.SUCCESS
//        );
//
//        return transactionRepository.save(transaction);
//    }
//
//    @Transactional
//    public BankTransaction withdraw(TransactionRequestDTO request) {
//        Account account = accountRepository.findById(request.getAccountId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
//
//        if (account.getBalance() < request.getAmount()) {
//            throw new IllegalArgumentException("Insufficient funds");
//        }
//
//        account.setBalance(account.getBalance() - request.getAmount());
//        accountRepository.save(account);
//
//        BankTransaction transaction = new BankTransaction(
//                TransactionType.WITHDRAW,
//                UserType.CUSTOMER,
//                account.getCustomer().getUser(),
//                account,
//                null,
//                request.getAmount(),
//                LocalDateTime.now(),
//                TransactionStatus.SUCCESS
//        );
//
//        return transactionRepository.save(transaction);
//    }

    @Transactional
    public BankTransactionDTO deposit(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));

        double todayTotalTransactions = getTodayTotalTransactions(account);
        if (todayTotalTransactions + request.getAmount() > account.getDailyLimit()) {
            throw new IllegalArgumentException("Daily limit exceeded");
        }

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        BankTransaction transaction = new BankTransaction(
                TransactionType.DEPOSIT,
                UserType.CUSTOMER,
                account.getCustomer().getUser(),
                account,
                null,
                request.getAmount(),
                LocalDateTime.now(),
                TransactionStatus.SUCCESS
        );

        transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    @Transactional
    public BankTransactionDTO withdraw(TransactionRequestDTO request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));

        if (account.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        double todayTotalTransactions = getTodayTotalTransactions(account);
        if (todayTotalTransactions + request.getAmount() > account.getDailyLimit()) {
            throw new IllegalArgumentException("Daily limit exceeded");
        }

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        BankTransaction transaction = new BankTransaction(
                TransactionType.WITHDRAW,
                UserType.CUSTOMER,
                account.getCustomer().getUser(),
                account,
                null,
                request.getAmount(),
                LocalDateTime.now(),
                TransactionStatus.SUCCESS
        );

        transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    private double getTodayTotalTransactions(Account account) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<BankTransaction> todayTransactions = transactionRepository.findByFromAccountAndCurrentTimeBetween(account, startOfDay, endOfDay);
        return todayTransactions.stream().mapToDouble(BankTransaction::getTransferAmount).sum();
    }

    private BankTransactionDTO mapToDTO(BankTransaction transaction) {
        return new BankTransactionDTO(
                transaction.getType(),
                transaction.getInitiatedBy(),
                transaction.getUser().getFirstName(),
                transaction.getUser().getLastName(),
                transaction.getFromAccount() != null ? transaction.getFromAccount().getIBAN() : null,
                transaction.getToAccount() != null ? transaction.getToAccount().getIBAN() : null,
                transaction.getTransferAmount(),
                transaction.getCurrentTime().toString(),
                transaction.getStatus()
        );
    }
}
