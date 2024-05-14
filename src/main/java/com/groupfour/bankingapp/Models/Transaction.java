package com.groupfour.bankingapp.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType type;
    private UserType initiatedBy; // employee/customer
    @OneToOne
    private User user;
    @OneToOne
    private Account fromAccount;
    @OneToOne
    private Account toAccount;
    private Double transferAmount;
    private LocalDateTime currentTime;
    private TransactionStatus status;

    // Getters for all fields

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public UserType getInitiatedBy() {
        return initiatedBy;
    }

    public User getUser() {
        return user;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

}

