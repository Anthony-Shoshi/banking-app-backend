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
import org.springframework.stereotype.Repository;

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
        User user1 = new User("user@gmail.com", bCryptPasswordEncoder.encode("123"), "Ador","Negash", "09220029", "fsgdgssgr", UserType.ROLE_USER );
        User user2 = new User("user@gmail.com", bCryptPasswordEncoder.encode("123"), "Ador","Negash", "09220029", "fsgdgssgr", UserType.ROLE_EMPLOYEE );

//        Customer customer1= new Customer(user1, CustomerStatus.APPROVED, Gender.MALE);
        Customer customer2= new Customer(user1, CustomerStatus.PENDING,18 ,Gender.MALE);
        Customer customer3= new Customer(user2, CustomerStatus.PENDING,18 ,Gender.MALE);

//        Account Account1 = new Account(customer1, "DE89 3704 0044 0532 0130 12", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");
//        Account Account2 = new Account(customer1, "DE89 3704 0044 0532 0130 00", 100.00, 00.00, AccountType.SAVING, true, 50.00, AccountStatus.ACTIVE, "€");


        userRepository.save(user1);
//        customerRepository.save(customer1);
        customerRepository.save(customer2);
//        accountRepository.save(Account1);
//        accountRepository.save(Account2);
        //transactionRepository.save(tarns1);

    }
}