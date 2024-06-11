package com.groupfour.bankingapp.Models.DTO;

public record BankTransactionPostDTO(String fromAccountIban, String toAccountIban, double transferAmount) {
}
