package com.groupfour.bankingapp.Controllers;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ObjectMapper objectMapper;
    private JsonEncoder bCryptPasswordEncoder;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testTransferMoney() throws Exception {
        // Mock data
        String fromAccountIban = "IBAN123";
        String toAccountIban = "IBAN456";
        double transferAmount = 20.0;
        BankTransactionPostDTO transferRequest = new BankTransactionPostDTO(fromAccountIban, toAccountIban, transferAmount);
        BankTransactionPostDTO transactionDTO = new BankTransactionPostDTO(fromAccountIban, toAccountIban, transferAmount);

        // Mock the service call
        when(transactionService.transferMoney(fromAccountIban, toAccountIban, transferAmount)).thenReturn(transactionDTO);

        // Perform the request
        MockHttpServletResponse response = mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // Verify the response
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(transactionDTO));

        // Verify that the service method was called
        verify(transactionService, times(1)).transferMoney(fromAccountIban, toAccountIban, transferAmount);
    }


    @Test
    public void testGetAllTransactions() {
        when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());
        ResponseEntity<Object> response = transactionController.getAllTransactions();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    void getTransactionsByCustomerId() {
        // Mocking data
        Long customerId = 1L;
        List<BankTransactionDTO> mockTransactions = new ArrayList<>();
        // Add mock transactions to the list if needed

        // Mocking behavior of the transactionService
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(java.util.Optional.of(mockTransactions));

        // Calling the controller method
        ResponseEntity<Object> responseEntity = transactionController.getTransactionsByCustomerId(customerId);

        // Verifying the response
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockTransactions, responseEntity.getBody());

        // Verifying that transactionService method was called once with the correct parameter
        verify(transactionService, times(1)).getTransactionsByCustomerId(customerId);
    }

    @Test
    void getTransactionHistory() {
        // Mocking data
        Long customerId = 1L;
        String startDate = "2024-01-01";
        String endDate = "2024-06-01";
        Double fromAmount = 100.0;
        Double toAmount = 500.0;
        String iban = "ABC123";

        List<BankTransactionDTO> mockTransactions = new ArrayList<>();
        // Add mock transactions to the list if needed

        // Mocking behavior of the transactionService
        when(transactionService.getTransactionHistory(customerId, LocalDate.parse(startDate), LocalDate.parse(endDate), fromAmount, toAmount, iban)).thenReturn(mockTransactions);

        // Calling the controller method
        ResponseEntity<List<BankTransactionDTO>> responseEntity = transactionController.getTransactionHistory(customerId, startDate, endDate, fromAmount, toAmount, iban);

        // Verifying the response
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockTransactions, responseEntity.getBody());

        // Verifying that transactionService method was called once with the correct parameters
        verify(transactionService, times(1)).getTransactionHistory(customerId, LocalDate.parse(startDate), LocalDate.parse(endDate), fromAmount, toAmount, iban);
    }


}
