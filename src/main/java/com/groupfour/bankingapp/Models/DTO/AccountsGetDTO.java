package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.AccountStatus;
import com.groupfour.bankingapp.Models.AccountType;

public record AccountsGetDTO(Long accountId,
                             Long customerId,
                             String customerName,
                             String IBAN,
                             Double balance,
                             AccountType accountType,
                             AccountStatus status,
                             Double absoluteLimit,
                             Double dailyLimit
                             ) {
}
