package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;

import java.time.LocalDateTime;

public record TransactionDTO(Long id, TransactionType type, UserType initiatedBy, Long userId,
                             Long fromAccountId, Long toAccountId, Double transferAmount,
                             LocalDateTime currentTime, TransactionStatus status) {
}
