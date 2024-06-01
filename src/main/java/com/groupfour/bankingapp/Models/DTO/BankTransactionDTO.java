package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;

public record BankTransactionDTO(TransactionType type,
                                 UserType initiatedBy,
                                 String firstName,
                                 String lastName,
                                 String fromAccountIban,
                                 String toAccountIban,
                                 Double transferAmount,
                                 String currentTime,
                                 TransactionStatus status) {
}
