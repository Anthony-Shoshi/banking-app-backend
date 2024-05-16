package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.BankTransaction;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

    }

    public List<BankTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

}
