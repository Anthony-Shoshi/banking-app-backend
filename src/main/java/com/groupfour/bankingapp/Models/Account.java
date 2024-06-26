package com.groupfour.bankingapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JsonIgnoreProperties({"accounts"})
    private Customer customer;

    @Column(unique = true)
    private String IBAN;

    private Double balance;
    private Double absoluteLimit;
    private AccountType accountType;
    private Boolean isActive;
    private Double dailyLimit;
    private AccountStatus status;
    private String currency;

    public Long getAccountId() {
        return accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getIBAN() {
        return IBAN;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Double getDailyLimit() {
        return dailyLimit;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDailyLimit(Double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getCurrency() {
        return currency;
    }

    public Account( Customer customer, String IBAN, Double balance, Double absoluteLimit, AccountType accountType, Boolean isActive, Double dailyLimit, AccountStatus status, String currency) {
        this.customer = customer;
        this.IBAN = IBAN;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.accountType = accountType;
        this.isActive = isActive;
        this.dailyLimit = dailyLimit;
        this.status = status;
        this.currency = currency;
    }
}
