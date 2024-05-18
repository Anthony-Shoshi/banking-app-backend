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
    private CustomerStatus status;  // Enum for status (PENDING, APPROVED, REJECTED)



    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user; // Link to the User entity

    // Default constructor
    public Customer() {}

    // Constructor with all fields
    public Customer(User user, CustomerStatus status, Gender gender) {
        this.user = user;
        this.status = status;
        this.gender = gender;
    }

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
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
