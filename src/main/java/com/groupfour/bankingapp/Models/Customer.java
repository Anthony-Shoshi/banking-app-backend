package com.groupfour.bankingapp.Models;


import jakarta.persistence.*;

//import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStauts status;  // Enum for status (PENDING, APPROVED, REJECTED)

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user; // Link to the User entity

    // Default constructor
    public Customer() {}

    // Constructor with all fields
    public Customer(User user, AccountStauts status, int age, Gender gender) {
        this.user = user;
        this.status = status;
        this.age = age;
        this.gender = gender;
    }

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public AccountStauts getStatus() {
        return status;
    }

    public void setStatus(AccountStauts status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
