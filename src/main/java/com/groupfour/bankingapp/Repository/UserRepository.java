package com.groupfour.bankingapp.Repository;

//import com.groupfour.bankingapp.Models.Transaction;
import com.groupfour.bankingapp.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id); // Method to find a user by ID
    User findByEmail(String email); // Method to find a user by email
    Optional<User> findUserByEmail(String email);
    User findByFirstName(String firstName);
}
