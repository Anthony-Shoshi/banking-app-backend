package com.groupfour.bankingapp.Repository;

//import com.groupfour.bankingapp.Models.Transaction;
import com.groupfour.bankingapp.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id); // Method to find a user by ID
}
