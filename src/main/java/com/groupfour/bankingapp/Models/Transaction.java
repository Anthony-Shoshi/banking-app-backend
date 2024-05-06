package com.groupfour.bankingapp.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType type;
    //private User initiatedBy; // employee/customer
    private Long userId;
   // @OneToOne
   // private User user;
    @OneToOne
    private Account fromAccount;
    @OneToOne
    private Account toAccount;
    private Double transferAmount;
    private LocalDateTime currentTime;
    private TransactionStatus status;
    private TransactionType transactionType;


}

