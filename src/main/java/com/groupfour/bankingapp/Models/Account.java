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
@AllArgsConstructor

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne
    @JsonIgnoreProperties({"accounts"})
   // private Customer customer;

    @Column(unique = true)
    private String IBAN;

    private Double balance;
    private Double absoluteLimit;
    private AccountType accountType;
    private Boolean isActive;
    private Double dailyLimit;
    private AccountStatus status;

}
