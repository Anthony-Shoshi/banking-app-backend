package com.groupfour.bankingapp.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
//@AllArgsConstructor

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

    public String getCurrency() {
        return currency;
    }

    public Account(
            //Long accountId,
                   Customer customer, String IBAN, Double balance, Double absoluteLimit, AccountType accountType, Boolean isActive, Double dailyLimit, AccountStatus status, String currency) {
      //  this.accountId = accountId;
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
