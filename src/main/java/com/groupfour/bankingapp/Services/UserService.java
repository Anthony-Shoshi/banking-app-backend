package com.groupfour.bankingapp.Services;

import javax.naming.AuthenticationException;

import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;
import com.groupfour.bankingapp.Repository.UserRepository;
import com.groupfour.bankingapp.Security.JwtProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {
        User user = userRepository.findByUsername(loginRequest.email());
        if (user != null && passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            return new LoginResponseDTO(user.getEmail(), jwtProvider.createToken(user.getEmail(), user.getRoles()));
        } else {
            throw new AuthenticationException("Invalid credentials");
        }
    }
}