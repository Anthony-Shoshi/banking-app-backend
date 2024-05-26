package com.groupfour.bankingapp.Models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String bsn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType role; // Role is either employee or customer

    @Column(nullable = false)
    private String DateOFbirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;
    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String email, String password, String firstName, String lastName, String phoneNumber, String bsn, UserType role, Gender gender, String DateOFbirth) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.bsn = bsn;
        this.role = role;
        this.gender = gender;
        this.DateOFbirth = DateOFbirth;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    private List<Role> roles;
    public void setRole(UserType role) {
        this.role = role;
    }

    public String getDateOFbirth() {
        return DateOFbirth;
    }

    public void setDateOFbirth(String dateOFbirth) {
        DateOFbirth = dateOFbirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
