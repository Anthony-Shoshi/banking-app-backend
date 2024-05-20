package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void getAllTransactionsShouldReturnTransactions() throws Exception {
        // Prepare a BankTransactionDTO object
        BankTransactionDTO transactionDTO = new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.CUSTOMER,
                "John",
                "Doe",
                "DE1234567890",
                "DE0987654321",
                100.0,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                TransactionStatus.SUCCESS
        );

        // Mock the service method
        when(transactionService.getAllTransactions()).thenReturn(List.of(transactionDTO));

        // Perform the GET request and verify the response
        this.mockMvc.perform(get("/transactions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value(transactionDTO.type().toString()))
                .andExpect(jsonPath("$[0].initiatedBy").value(transactionDTO.initiatedBy().toString()))
                .andExpect(jsonPath("$[0].firstName").value(transactionDTO.firstName()))
                .andExpect(jsonPath("$[0].lastName").value(transactionDTO.lastName()))
                .andExpect(jsonPath("$[0].fromAccountIban").value(transactionDTO.fromAccountIban()))
                .andExpect(jsonPath("$[0].toAccountIban").value(transactionDTO.toAccountIban()))
                .andExpect(jsonPath("$[0].transferAmount").value(transactionDTO.transferAmount()))
                .andExpect(jsonPath("$[0].currentTime").value(transactionDTO.currentTime()))
                .andExpect(jsonPath("$[0].status").value(transactionDTO.status().toString()));
    }

    @Test
    void getTransactionsByCustomerIdShouldReturnTransactions() throws Exception {
        // Prepare a BankTransactionDTO object
        Long customerId = 1L;
        BankTransactionDTO transactionDTO = new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.CUSTOMER,
                "John",
                "Doe",
                "DE1234567890",
                "DE0987654321",
                100.0,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                TransactionStatus.SUCCESS
        );

        // Mock the service method
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(Optional.of(List.of(transactionDTO)));

        // Perform the GET request and verify the response
        this.mockMvc.perform(get("/customers/{customerId}/transactions", customerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value(transactionDTO.type().toString()))
                .andExpect(jsonPath("$[0].initiatedBy").value(transactionDTO.initiatedBy().toString()))
                .andExpect(jsonPath("$[0].firstName").value(transactionDTO.firstName()))
                .andExpect(jsonPath("$[0].lastName").value(transactionDTO.lastName()))
                .andExpect(jsonPath("$[0].fromAccountIban").value(transactionDTO.fromAccountIban()))
                .andExpect(jsonPath("$[0].toAccountIban").value(transactionDTO.toAccountIban()))
                .andExpect(jsonPath("$[0].transferAmount").value(transactionDTO.transferAmount()))
                .andExpect(jsonPath("$[0].currentTime").value(transactionDTO.currentTime()))
                .andExpect(jsonPath("$[0].status").value(transactionDTO.status().toString()));
    }

    @Test
    void getTransactionsByCustomerIdShouldReturnNotFound() throws Exception {
        Long customerId = 1L;

        // Mock the service method to return an empty optional
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(Optional.empty());

        // Perform the GET request and verify the response
        this.mockMvc.perform(get("/customers/{customerId}/transactions", customerId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
