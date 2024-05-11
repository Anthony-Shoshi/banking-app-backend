package com.groupfour.bankingapp.Repository;
import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {

    User findByUsername(String username);

}
