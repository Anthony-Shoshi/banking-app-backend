package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

}
