package com.groupfour.bankingapp.Exception;

import javax.naming.AuthenticationException;

public class AccountPendingException extends AuthenticationException {

    public AccountPendingException(String msg, Throwable t) {
        super(msg);
    }

    public AccountPendingException(String msg) {
        super(msg);
    }
}
