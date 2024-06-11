package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Services.AccountService;
import com.groupfour.bankingapp.Services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerAccountTransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionController transactionController;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController, accountController).build();
    }

    @Test
    void getTransactionHistory_Success() throws Exception {
        List<BankTransactionDTO> transactions = List.of(
                new BankTransactionDTO(TransactionType.DEPOSIT, UserType.ROLE_CUSTOMER, "Ador", "Negash", "DE89 3704 0044 0532 0130 14", "DE89 3704 0044 0532 0130 00", 25.0, "2024-06-07", TransactionStatus.SUCCESS),
                new BankTransactionDTO(TransactionType.WITHDRAW, UserType.ROLE_CUSTOMER, "Ador", "Negash", "DE89 3704 0044 0532 0130 14", "DE89 3704 0044 0532 0130 14", 20.0, "2024-06-07", TransactionStatus.SUCCESS)
        );
        when(transactionService.getTransactionHistory(anyLong(), any(), any(), any(), any(), anyString())).thenReturn(transactions);

        mockMvc.perform(get("/customers/transaction-history")
                        .param("customerId", "1")
                        .param("startDate", "2024-06-01")
                        .param("endDate", "2024-06-30")
                        .param("fromAmount", "1")
                        .param("toAmount", "900")
                        .param("iban", "DE89 3704 0044 0532 0130 14")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"type\":\"DEPOSIT\",\"initiatedBy\":\"ROLE_CUSTOMER\",\"firstName\":\"Ador\",\"lastName\":\"Negash\",\"fromAccountIban\":\"DE89 3704 0044 0532 0130 14\",\"toAccountIban\":\"DE89 3704 0044 0532 0130 00\",\"transferAmount\":25.0,\"currentTime\":\"2024-06-07\",\"status\":\"SUCCESS\"},{\"type\":\"WITHDRAW\",\"initiatedBy\":\"ROLE_CUSTOMER\",\"firstName\":\"Ador\",\"lastName\":\"Negash\",\"fromAccountIban\":\"DE89 3704 0044 0532 0130 14\",\"toAccountIban\":\"DE89 3704 0044 0532 0130 14\",\"transferAmount\":20.0,\"currentTime\":\"2024-06-07\",\"status\":\"SUCCESS\"}]"));
    }

    @Test
    void getIbansByCustomerName_Success() throws Exception {
        String iban = "DE89 3704 0044 0532 0130 14";
        when(accountService.getCurrentAccountIbanByCustomerName(anyString(), anyString())).thenReturn(iban);

        mockMvc.perform(get("/customers/search-iban")
                        .param("firstName", "Ador")
                        .param("lastName", "Negash")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"iban\":\"DE89 3704 0044 0532 0130 14\"}"));
    }

    @Test
    void getAccountsByUserId_Success() throws Exception {
        List<AccountsGetDTO> accounts = List.of(
                new AccountsGetDTO(1L, 1L, "Ador Negash", "DE89 3704 0044 0532 0130 14", 91.0, AccountType.CURRENT, CustomerStatus.APPROVED, 0.0, 50.0),
                new AccountsGetDTO(2L, 1L, "Ador Negash", "DE89 3704 0044 0532 0130 00", 109.0, AccountType.SAVING, CustomerStatus.APPROVED, 0.0, 50.0)
        );
        when(accountService.getAccountsByUserId(anyLong())).thenReturn(accounts);

        mockMvc.perform(get("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"accountId\":1,\"customerId\":1,\"customerName\":\"Ador Negash\",\"IBAN\":\"DE89 3704 0044 0532 0130 14\",\"balance\":91.0,\"accountType\":\"CURRENT\",\"status\":\"APPROVED\",\"absoluteLimit\":0.0,\"dailyLimit\":50.0},{\"accountId\":2,\"customerId\":1,\"customerName\":\"Ador Negash\",\"IBAN\":\"DE89 3704 0044 0532 0130 00\",\"balance\":109.0,\"accountType\":\"SAVING\",\"status\":\"APPROVED\",\"absoluteLimit\":0.0,\"dailyLimit\":50.0}]"));
    }
}