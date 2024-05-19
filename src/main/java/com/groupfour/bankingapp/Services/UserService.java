package com.groupfour.bankingapp.Services;

import javax.naming.AuthenticationException;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

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

//            if (customer == null) {
//                throw new AuthenticationException("Customer record not found for user.");
//            }
             boolean isApproved = customer.getStatus() == CustomerStatus.APPROVED;
            return new LoginResponseDTO(user.getEmail(), jwtTokenProvider.createToken(user.getUserId(), user.getRole(), isApproved));
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}
