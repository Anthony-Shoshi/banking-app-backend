package com.groupfour.bankingapp.Models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BankTransactionTest {

    @Test
    void testConstructorAndGetters() {
        // Prepare test data
        TransactionType type = TransactionType.DEPOSIT;
        UserType initiatedBy = UserType.CUSTOMER;
        User user = new User(); // Assuming User class has a default constructor
        Account fromAccount = new Account(); // Assuming Account class has a default constructor
        Account toAccount = new Account(); // Assuming Account class has a default constructor
        Double transferAmount = 100.0;
        LocalDateTime currentTime = LocalDateTime.now();
        TransactionStatus status = TransactionStatus.SUCCESS;

        // Create a BankTransaction object using the constructor
        BankTransaction transaction = new BankTransaction(type, initiatedBy, user, fromAccount, toAccount, transferAmount, currentTime, status);

        // Verify the values using the getter methods
        assertEquals(type, transaction.getType());
        assertEquals(initiatedBy, transaction.getInitiatedBy());
        assertEquals(user, transaction.getUser());
        assertEquals(fromAccount, transaction.getFromAccount());
        assertEquals(toAccount, transaction.getToAccount());
        assertEquals(transferAmount, transaction.getTransferAmount());
        assertEquals(currentTime, transaction.getCurrentTime());
        assertEquals(status, transaction.getStatus());
    }

    @Test
    void testDefaultConstructor() {
        // Create a BankTransaction object using the default constructor
        BankTransaction transaction = new BankTransaction();

        // Verify that all fields are null or default values
        assertNull(transaction.getId());
        assertNull(transaction.getType());
        assertNull(transaction.getInitiatedBy());
        assertNull(transaction.getUser());
        assertNull(transaction.getFromAccount());
        assertNull(transaction.getToAccount());
        assertNull(transaction.getTransferAmount());
        assertNull(transaction.getCurrentTime());
        assertNull(transaction.getStatus());
    }
}
