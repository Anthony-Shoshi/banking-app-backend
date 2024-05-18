package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.BankTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findAll(Specification<BankTransaction> specification, Pageable pageable);
}

