package com.groupfour.bankingapp.Models;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    ROLE_CUSTOMER,
    ROLE_EMPLOYEE,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }

}
