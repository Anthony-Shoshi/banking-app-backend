package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.TransactionRequestDTO;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerTransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CustomerTransactionController customerTransactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerTransactionController).build();
    }

    @Test
    void deposit_Success() throws Exception {
        BankTransactionDTO transactionDTO = new BankTransactionDTO(
                TransactionType.DEPOSIT,
                UserType.ROLE_CUSTOMER,
                "Ador",
                "Negash",
                "DE89 3704 0044 0532 0130 14",
                null,
                5.0,
                "2024-06-01T04:33:58.821569900",
                TransactionStatus.SUCCESS
        );
        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenReturn(transactionDTO);

        mockMvc.perform(post("/customers/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": 5.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"type\": \"DEPOSIT\", \"initiatedBy\": \"ROLE_CUSTOMER\", \"firstName\": \"Ador\", \"lastName\": \"Negash\", \"fromAccountIban\": \"DE89 3704 0044 0532 0130 14\", \"toAccountIban\": null, \"transferAmount\": 5.0, \"currentTime\": \"2024-06-01T04:33:58.821569900\", \"status\": \"SUCCESS\" }"));
    }

    @Test
    void deposit_BadRequest() throws Exception {
        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid request"));

        mockMvc.perform(post("/customers/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": -100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid request"));
    }

    @Test
    void deposit_InternalServerError() throws Exception {
        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/customers/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": 100.0}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred"));
    }

    @Test
    void withdraw_Success() throws Exception {
        BankTransactionDTO transactionDTO = new BankTransactionDTO(
                TransactionType.WITHDRAW,
                UserType.ROLE_CUSTOMER,
                "Ador",
                "Negash",
                "DE89 3704 0044 0532 0130 14",
                null,
                1.0,
                "2024-06-04T22:37:57.047884",
                TransactionStatus.SUCCESS
        );
        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenReturn(transactionDTO);

        mockMvc.perform(post("/customers/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": 1.0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"type\": \"WITHDRAW\", \"initiatedBy\": \"ROLE_CUSTOMER\", \"firstName\": \"Ador\", \"lastName\": \"Negash\", \"fromAccountIban\": \"DE89 3704 0044 0532 0130 14\", \"toAccountIban\": null, \"transferAmount\": 1.0, \"currentTime\": \"2024-06-04T22:37:57.047884\", \"status\": \"SUCCESS\" }"));
    }

    @Test
    void withdraw_BadRequest() throws Exception {
        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid request"));

        mockMvc.perform(post("/customers/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": -100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid request"));
    }

    @Test
    void withdraw_InternalServerError() throws Exception {
        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/customers/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\": 1, \"amount\": 100.0}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred"));
    }
}