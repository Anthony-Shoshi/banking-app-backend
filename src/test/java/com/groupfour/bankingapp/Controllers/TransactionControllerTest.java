package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Models.Gender;
import com.groupfour.bankingapp.Models.User;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
        ResponseEntity<List<BankTransactionDTO>> responseEntity = transactionController.getTransactionsByCustomerId(customerId);

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
