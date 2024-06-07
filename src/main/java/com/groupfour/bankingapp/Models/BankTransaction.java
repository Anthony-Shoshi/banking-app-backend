package com.groupfour.bankingapp.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BANK_TRANSACTION")
public class BankTransaction {
    public void setId(Long id) {
        this.id = id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setInitiatedBy(UserType initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

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
    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

}