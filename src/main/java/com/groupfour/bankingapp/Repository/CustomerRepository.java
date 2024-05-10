package com.groupfour.bankingapp.Repository;

import com.groupfour.bankingapp.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.status = 'PENDING' AND c NOT IN (SELECT a.customer FROM Account a)")
    List<Customer> findCustomersWithoutAccounts();
}
