package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Config.BeanFactory;
import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private final AccountRepository accountRepository;
    private final BeanFactory beanFactory;

    private final UserService userService;
    private final HttpServletRequest request;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, BeanFactory beanFactory, UserService userService, HttpServletRequest request) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.beanFactory = beanFactory;
        this.userService= userService;
        this.request = request;
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

    //test needed
    protected double getTotalTransferredAmountToday(Account fromAccount) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<BankTransaction> todayTransactions = transactionRepository.findByFromAccountAndCurrentTimeBetween(
                fromAccount, startOfDay, endOfDay);

        return todayTransactions.stream()
                .mapToDouble(BankTransaction::getTransferAmount)
                .sum();
    }

    @Transactional
    public BankTransactionPostDTO transferMoney(String fromAccountIban, String toAccountIban, double transferAmount) {
        Account fromAccount = getAccountByIban(fromAccountIban);
        Account toAccount = getAccountByIban(toAccountIban);

        validateAccounts(fromAccount, toAccount);
        validateSufficientFunds(fromAccount, transferAmount);
        validateDailyLimit(fromAccount, transferAmount);

        executeTransfer(fromAccount, toAccount, transferAmount);

        BankTransaction transaction = createTransaction(fromAccount, toAccount, transferAmount);
        transactionRepository.save(transaction);

        return createBankTransactionPostDTO(fromAccountIban, toAccountIban, transferAmount);
    }

    protected void validateDailyLimit(Account fromAccount, double transferAmount) {
        double totalTransferredToday = getTotalTransferredAmountToday(fromAccount);
        if (totalTransferredToday + transferAmount > fromAccount.getDailyLimit()) {
            throw new IllegalArgumentException("Transfer amount exceeds the daily limit");
        }
    }


    protected Account getAccountByIban(String iban) {
        return accountRepository.findByIBAN(iban);
    }

    protected void validateAccounts(Account fromAccount, Account toAccount) {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Source or destination account not found");
        }
    }

    protected void validateSufficientFunds(Account fromAccount, double transferAmount) {
        if (fromAccount.getBalance() < transferAmount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    protected void executeTransfer(Account fromAccount, Account toAccount, double transferAmount) {
        fromAccount.setBalance(fromAccount.getBalance() - transferAmount);
        toAccount.setBalance(toAccount.getBalance() + transferAmount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    protected BankTransaction createTransaction(Account fromAccount, Account toAccount, double transferAmount) {
        User initiator = null;
        try {
            initiator = userService.getCurrentLoggedInUser(request);
            if (initiator == null) {
                throw new NullPointerException("Initiator is null");
            }
        } catch (Exception e) {
            System.err.println("Failed to create transaction: " + e.getMessage());
            return null;
        }

        return new BankTransaction(
                TransactionType.TRANSFER,
                UserType.ROLE_EMPLOYEE,
                initiator,
                fromAccount,
                toAccount,
                transferAmount,
                LocalDateTime.now(),
                TransactionStatus.SUCCESS
        );
    }
    protected BankTransactionPostDTO createBankTransactionPostDTO(String fromAccountIban, String toAccountIban, double transferAmount) {
        return new BankTransactionPostDTO(
                fromAccountIban,
                toAccountIban,
                transferAmount
        );
    }





}
