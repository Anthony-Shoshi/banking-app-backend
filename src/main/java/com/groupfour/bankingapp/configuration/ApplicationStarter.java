package com.groupfour.bankingapp.configuration;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
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
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    public ApplicationStarter(TransactionService transactionService, TransactionRepository transactionRepository, UserRepository userRepository, AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.transactionService = transactionService;
        this.transactionRepository= transactionRepository;
        this.userRepository = userRepository;
        this.accountRepository= accountRepository;
        this.customerRepository= customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        User user3 = new User("mahbaan77@gmail.com","123", "Fateme","Sabagh", "0648673055", "fsgdgssgr", UserType.CUSTOMER, Gender.FEMALE, "11-11-2000");
        User user4 = new User("jon4@gmail.com","1234", "Jon","Smith", "09220029", "fsgdgs00", UserType.CUSTOMER, Gender.MALE, "21-10-2000" );
        Customer customer3= new Customer(user3, CustomerStatus.APPROVED);
        Customer customer4= new Customer(user4, CustomerStatus.APPROVED);
        Account Account3 = new Account(customer3, "DE89 3704 0044 0532 0130 12", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");
        Account Account4 = new Account(customer4, "DE89 3704 0044 0532 0130 11", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");

        userRepository.save(user3);
        userRepository.save(user4);
        customerRepository.save(customer3);
        customerRepository.save(customer4);
        accountRepository.save(Account3);
        accountRepository.save(Account4);


        if (user3 != null) {
            BankTransaction t = new BankTransaction(
                    TransactionType.DEPOSIT,
                    UserType.CUSTOMER,
                    user3,
                    Account3,
                    Account4,
                    1000.00, // Transfer Amount
                    LocalDateTime.now(),
                    TransactionStatus.SUCCESS
            );
            transactionRepository.save(t);
        } else {
            System.out.println("User not found with ID: 1234");
        }

       List<BankTransactionDTO> allBankTransactions = transactionService.getAllTransactions();
       // allBankTransactions.forEach(bankTransaction -> System.out.println("BankTransaction ID: " ));
    }
}


