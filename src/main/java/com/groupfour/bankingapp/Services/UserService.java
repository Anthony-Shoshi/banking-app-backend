package com.groupfour.bankingapp.Services;

//import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        User user = userRepository.findByEmail(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            Customer customer = customerRepository.findByUserUserId(user.getUserId());
            if (customer != null && customer.getStatus() == CustomerStatus.APPROVED) {
                String token = jwtTokenProvider.createToken(user.getUserId(), user.getRole(), true); // Always set approved to true here
                return new LoginResponseDTO(user.getEmail(), token, user.getRole().name(), user.getFirstName(), user.getLastName());
            } else {
                throw new AuthenticationException("Your Account has not been approved yet. Please wait for approval.");
            }
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
    public User getCurrentLoggedInUser(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null) {
                System.err.println("Token is null");
                return null;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                System.err.println("Token is invalid");
                return null;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            if (authentication == null) {
                System.err.println("Authentication is null");
                return null;
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails == null) {
                System.err.println("UserDetails is null");
                return null;
            }

            String userIdString = userDetails.getUsername();
            Long userId = Long.parseLong(userIdString); // Convert the user ID string to Long
            User user = userRepository.findById(userId).orElse(null); // Retrieve user by ID
            if (user == null) {
                System.err.println("No user found with ID: " + userId);
            }

            return user;
        } catch (Exception e) {
            // Log the exception
            System.err.println("An error occurred while retrieving the logged-in user: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more detailed debugging
        }
        return null;
    }



}
