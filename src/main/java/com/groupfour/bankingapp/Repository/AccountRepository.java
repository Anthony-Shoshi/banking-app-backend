package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository <Account, Long>{
    boolean existsByIBAN(String iban);
}
