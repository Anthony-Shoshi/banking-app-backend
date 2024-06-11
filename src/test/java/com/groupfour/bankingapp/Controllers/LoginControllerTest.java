package com.groupfour.bankingapp.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;
import com.groupfour.bankingapp.Services.AccountService;
import com.groupfour.bankingapp.Services.UserService;

import javax.naming.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_WithValidCredentials_ReturnsOk() throws Exception {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("faizan@gmail.com", "2345");
        String token = "sampleToken";
        when(userService.login(loginRequestDTO)).thenReturn(new LoginResponseDTO("username", token));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));
    }

    @Test
    void login_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("faizan@gmail.com", "123");
        // Mock the behavior of userService.login to throw AuthenticationException
        doThrow(new AuthenticationException("Invalid credentials")).when(userService).login(loginRequestDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"faizan@gmail.com\",\"password\":\"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
