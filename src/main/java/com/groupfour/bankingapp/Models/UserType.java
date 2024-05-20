package com.groupfour.bankingapp.Models;

public enum UserType {
    CUSTOMER,
    EMPLOYEE,
    USER;

    public String getAuthority() {
        return name();
    }

}
