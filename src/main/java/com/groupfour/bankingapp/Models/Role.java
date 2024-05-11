package com.groupfour.bankingapp.Models;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    customer, employee;

    @Override
    public String getAuthority() {
        return name();
    }
}