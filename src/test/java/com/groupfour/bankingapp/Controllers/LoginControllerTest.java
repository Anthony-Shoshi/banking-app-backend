package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;
import com.groupfour.bankingapp.Services.UserService;
import com.groupfour.bankingapp.Controllers.LoginController;

import javax.naming.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void login_WithValidCredentials_ReturnsOk() throws Exception {
        // Arrange
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("faizan@gmail.com", "2345");
        String token = "sampleToken";
        when(userService.login(loginRequestDTO)).thenReturn(new LoginResponseDTO("username", token));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"faizan@gmail.com\",\"password\":\"2345\"}"))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"faizan@gmail.com\",\"password\":\"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
