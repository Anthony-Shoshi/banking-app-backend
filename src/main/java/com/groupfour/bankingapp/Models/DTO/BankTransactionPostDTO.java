package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.User;

public record BankTransactionPostDTO (String fromAccountIban, String toAccountIban, double transferAmount

){

}