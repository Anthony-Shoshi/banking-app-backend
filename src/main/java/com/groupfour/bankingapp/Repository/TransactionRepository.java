package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.Account;
import com.groupfour.bankingapp.Models.BankTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findAll(Specification<BankTransaction> specification, Pageable pageable);
    List<BankTransaction> findByFromAccountCustomerCustomerId(Long customerId);
    List<BankTransaction> findByFromAccountAndCurrentTimeBetween(Account fromAccount, LocalDateTime start, LocalDateTime end);
    @Query("SELECT t FROM BankTransaction t " +
            "WHERE (:customerId IS NULL OR t.fromAccount.customer.customerId = :customerId) " +
            "AND (:startDate IS NULL OR t.currentTime >= :startDate) " +
            "AND (:endDate IS NULL OR t.currentTime <= :endDate) " +
            "AND (:fromAmount IS NULL OR t.transferAmount >= :fromAmount) " +
            "AND (:toAmount IS NULL OR t.transferAmount <= :toAmount) " +
            "AND (:iban IS NULL OR t.fromAccount.IBAN = :iban OR t.toAccount.IBAN = :iban)")
    List<BankTransaction> findFilteredTransactions(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("fromAmount") Double fromAmount,
            @Param("toAmount") Double toAmount,
            @Param("iban") String iban);
}

