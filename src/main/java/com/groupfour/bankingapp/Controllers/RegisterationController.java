package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.Gender;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Models.DTO.RegisterRequestDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
public class RegisterationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = Logger.getLogger(RegisterationController.class.getName());

    @PostMapping
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO requestDTO) {
        logger.info("Received registration request");

        // Validate email format
        if (!Pattern.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", requestDTO.email())) {
            logger.warning("Invalid email format");
            return ResponseEntity.badRequest().body("Invalid email format. Please provide a valid email address.");
        }

        // Validate password
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", requestDTO.password())) {
            logger.warning("Invalid password format");
            return ResponseEntity.badRequest().body("Invalid password format. Password must contain at least 8 characters including uppercase, lowercase, numbers, and special characters.");
        }

        // Validate BSN format
        if (!Pattern.matches("\\d{9}", requestDTO.bsn())) {
            logger.warning("Invalid BSN format");
            return ResponseEntity.badRequest().body("Invalid BSN format. Please provide a valid BSN (9 digits).");
        }

        // Validate phone number format
        if (!Pattern.matches("\\d{9}", requestDTO.phoneNumber())) {
            logger.warning("Invalid phone number format");
            return ResponseEntity.badRequest().body("Invalid phone number format. Please provide a valid phone number (9 digits).");
        }

        // Create new User object
        User user = new User(
                requestDTO.email(),
                bCryptPasswordEncoder.encode(requestDTO.password()),
                requestDTO.firstName(),
                requestDTO.lastName(),
                requestDTO.phoneNumber(),
                requestDTO.bsn(),
                UserType.CUSTOMER,
                Gender.valueOf(requestDTO.gender().toUpperCase()),
                requestDTO.DateOFbirth()
        );

        // Save user to the database
        userRepository.save(user);

        // Create new Customer object
        Customer customer = new Customer(user, CustomerStatus.PENDING);

        // Save customer to the database
        customerRepository.save(customer);

        logger.info("Registration successful");

        // Return success response
        return ResponseEntity.ok("Registration successful");
    }
}