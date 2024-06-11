package com.groupfour.bankingapp.Config;

import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory {
    private final UserRepository userRepository;

    public BeanFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        User user = (User) authentication.getPrincipal();
        try {
            return Long.parseLong(user.getFirstName()+user.getLastName());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void validate() throws Exception {
        if (this.getCurrentUserId() == null) {
            throw new Exception("User not authenticated");
        }
    }

    public User CurrentUser() throws IllegalArgumentException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        User user = (User) authentication.getPrincipal();
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }


        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}