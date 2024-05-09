package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.AccountStatus;
import com.groupfour.bankingapp.Models.AccountType;
import com.groupfour.bankingapp.Models.CustomerStatus;

public record AccountsGetDTO(Long accountId,
                             Long customerId,
                             String customerName,
                             String IBAN,
                             Double balance,
                             AccountType accountType,
                             CustomerStatus status,
                             Double absoluteLimit,
                             Double dailyLimit
                             ) {
}
