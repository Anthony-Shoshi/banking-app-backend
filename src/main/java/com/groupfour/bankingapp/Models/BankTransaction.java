package com.groupfour.bankingapp.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private UserType initiatedBy; // employee/customer

    @ManyToOne
    private User user;

    @ManyToOne
    private Account fromAccount;

    @ManyToOne
    private Account toAccount;
    @Column
    private Double transferAmount;

    @Column(name = "transaction_time")
    private LocalDateTime currentTime;


    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public BankTransaction() {
        // Default constructor required by JPA
    }

    public BankTransaction(TransactionType type, UserType initiatedBy, User user, Account fromAccount, Account toAccount, Double transferAmount, LocalDateTime currentTime, TransactionStatus status) {
        this.type = type;
        this.initiatedBy = initiatedBy;
        this.user = user;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.currentTime = currentTime;
        this.status = status;
    }

    // Getters and setters
}