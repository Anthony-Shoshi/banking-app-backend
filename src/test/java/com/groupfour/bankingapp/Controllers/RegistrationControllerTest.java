package com.groupfour.bankingapp.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfour.bankingapp.Models.DTO.RegisterRequestDTO;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private RegisterationController registerationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerationController).build();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterRequestDTO userDTO = new RegisterRequestDTO("test@example.com", "Test@123", "firstName", "lastName", "123456789", "123456789", "Male", "1990, 1, 1");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration successful")); 
    }

}
