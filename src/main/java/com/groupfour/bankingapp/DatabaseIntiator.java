package com.groupfour.bankingapp;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseIntiator implements ApplicationRunner {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountRepository accountRepository;
    private  final CustomerRepository customerRepository;
    private  final TransactionRepository transactionRepository;

    private  final UserRepository userRepository;


    public DatabaseIntiator(BCryptPasswordEncoder bCryptPasswordEncoder, AccountRepository accountRepository,CustomerRepository customerRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountRepository= accountRepository;
        this.customerRepository= customerRepository;
        this.transactionRepository= transactionRepository;
        this.userRepository= userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initiator();
    }

    private void initiator(){


        User user1 = new User("user@gmail.com", bCryptPasswordEncoder.encode("123"), "Ador","Negash", "09220029", "fsgdgssgr", UserType.CUSTOMER ,Gender.MALE, "11-11-2000");
        User user2 = new User("faizan@gmail.com", bCryptPasswordEncoder.encode("2345"), "Muhammad","Faizan", "06787545", "765569753", UserType.EMPLOYEE ,Gender.MALE, "21-10-2003");
        User user3 = new User("mahbaan77@gmail.com", "123", "Fateme", "Sabagh", "0648673055", "fsgdgssgr", UserType.CUSTOMER, Gender.FEMALE, "11-11-2000");
        User user4 = new User("jon4@gmail.com", "1234", "Jon", "Smith", "09220029", "fsgdgs00", UserType.CUSTOMER, Gender.MALE, "21-10-2000");

        Customer customer1= new Customer(user1, CustomerStatus.PENDING);
        Customer customer2= new Customer(user2, CustomerStatus.PENDING);
        Customer customer3 = new Customer(user3, CustomerStatus.APPROVED);
        Customer customer4 = new Customer(user4, CustomerStatus.APPROVED);

        Account Account1 = new Account(customer1, "DE89 3704 0044 0532 0130 14", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");
        Account Account2 = new Account(customer1, "DE89 3704 0044 0532 0130 00", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");
        Account Account3 = new Account(customer3, "DE89 3704 0044 0532 0130 12", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");
        Account Account4 = new Account(customer4, "DE89 3704 0044 0532 0130 11", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

        accountRepository.save(Account1);
        accountRepository.save(Account2);
        accountRepository.save(Account3);
        accountRepository.save(Account4);

        if (user3 != null) {
            BankTransaction tarnsaction2 = new BankTransaction(
                    TransactionType.DEPOSIT,
                    UserType.CUSTOMER,
                    user3,
                    Account3,
                    Account4,
                    1000.00, // Transfer Amount
                    LocalDateTime.now(),
                    TransactionStatus.SUCCESS
            );
            transactionRepository.save(tarnsaction2);
        } else {
            System.out.println("User not found with ID: 1234");
        }

        BankTransaction tarnsaction1 = new BankTransaction(TransactionType.DEPOSIT, UserType.CUSTOMER, user1, Account1, Account2, 25.00, LocalDateTime.now(), TransactionStatus.SUCCESS );
        transactionRepository.save(tarnsaction1);

    }
}