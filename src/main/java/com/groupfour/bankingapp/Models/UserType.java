package com.groupfour.bankingapp.Models;

public enum UserType {
    CUSTOMER,
    ROLE_EMPLOYEE,
    ROLE_USER;

    public String getAuthority() {
        return name();
    }

}
