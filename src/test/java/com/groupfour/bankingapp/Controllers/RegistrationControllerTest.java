package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.Customer;
import com.groupfour.bankingapp.Models.DTO.RegisterRequestDTO;
import com.groupfour.bankingapp.Models.Gender;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Repository.CustomerRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {


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
    }

    @Test
    void register_WithValidRequest_ReturnsOk() {
        // Arrange
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "test@example.com",
                "Test123@",
                "John",
                "Doe",
                "123456789",
                "123456789",
                "Male",
                "1990, 1, 1"
        );

        User user = new User(
                requestDTO.email(),
                requestDTO.password(),
                requestDTO.firstName(),
                requestDTO.lastName(),
                requestDTO.phoneNumber(),
                requestDTO.bsn(),
                UserType.CUSTOMER,
                Gender.MALE,
                requestDTO.DateOFbirth()
        );

        when(bCryptPasswordEncoder.encode(requestDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<Object> responseEntity = registerationController.register(requestDTO);

        // Assert
        assertEquals(ResponseEntity.ok("Registration successful"), responseEntity);
        verify(userRepository, times(1)).save(any(User.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

}


