package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <Account, Long>{
}
