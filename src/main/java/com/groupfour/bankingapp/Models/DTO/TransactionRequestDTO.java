package com.groupfour.bankingapp.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionRequestDTO {
    private Long accountId;
    private Double amount;

    public TransactionRequestDTO() {}

    public TransactionRequestDTO(Long accountId, Double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

}
