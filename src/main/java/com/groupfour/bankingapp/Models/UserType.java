package com.groupfour.bankingapp.Models;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    CUSTOMER,
    EMPLOYEE,
    USER;

    public String getAuthority() {
        return name();
    }

}
