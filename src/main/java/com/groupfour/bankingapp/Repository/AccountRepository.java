package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository <Account, Long>{
    boolean existsByIBAN(String iban);

    @Query("SELECT a FROM Account a WHERE a.customer.user.userId = :userId")
    List<Account> findByCustomerId(@Param("userId") Long userId);
}
