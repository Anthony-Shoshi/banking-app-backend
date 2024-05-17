package com.groupfour.bankingapp.Models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType type;
    private UserType initiatedBy; // employee/customer

   @OneToOne
    private Customer customer;
    @OneToOne
    private Account fromAccount;
    @OneToOne
    private Account toAccount;
    private Double transferAmount;
    private LocalDateTime currentTimeNow;
    private TransactionStatus status;

    private TransactionType transactionType;

    public Transaction(TransactionType type, UserType initiatedBy, Customer customer , Account fromAccount, Account toAccount, Double transferAmount, LocalDateTime currentTime, TransactionStatus status, TransactionType transactionType) {
        this.type = type;
        this.initiatedBy = initiatedBy;
        this.customer = customer;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.currentTimeNow = currentTime;
        this.status = status;
        this.transactionType = transactionType;
    }

    public Transaction() {
    }
}

