package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Models.AccountType;
import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
import com.groupfour.bankingapp.Models.DTO.UpdateAccountLimitsDTO;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.AccountService;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Arrays;


@WebMvcTest (AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;
    @Test
    @WithMockUser(username = "admin", roles = {"EMPLOYEE"})
    void getAllAccountsShouldReturnAccounts() throws Exception {
        when(accountService.getAllAccountDetails()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/employees/customer-accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"EMPLOYEE"})
    void getAllAccountsShouldReturnAccountss() throws Exception {
        // Setup sample data
        AccountsGetDTO sampleAccountDTO = new AccountsGetDTO(
                1L,  // accountId
                1L,  // customerId
                "John Doe",  // customerName
                "NL21INHO0123400081",  // IBAN
                100.00,  // balance
                AccountType.SAVING,  // accountType (assuming this is correct and enum is SAVING)
                CustomerStatus.APPROVED,  // status
                0.00,  // absoluteLimit
                500.00  // dailyLimit
        );

        // Mock the service method to return this sample data
        when(accountService.getAllAccountDetails()).thenReturn(Collections.singletonList(sampleAccountDTO));

        // Perform the request and assert the response
        mockMvc.perform(get("/employees/customer-accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(1)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")))
                .andExpect(jsonPath("$[0].IBAN", is("NL21INHO0123400081")))
                .andExpect(jsonPath("$[0].balance", is(100.0)))
                .andExpect(jsonPath("$[0].accountType", is("SAVING")))  // Adjusted to match actual enum value
                .andExpect(jsonPath("$[0].status", is("APPROVED")))
                .andExpect(jsonPath("$[0].absoluteLimit", is(0.0)))
                .andExpect(jsonPath("$[0].dailyLimit", is(500.0)));
    }
    @Test
    @WithMockUser(username="testUser", roles={"CUSTOMER"})
    void getAccountDetailsWhenAccountsExist() throws Exception {
        Long userId = 1L;
        AccountsGetDTO dto = new AccountsGetDTO(1L, 1L, "John Doe", "NL21INHO0123400081", 100.0, AccountType.SAVING, CustomerStatus.APPROVED, 0.0, 500.0);
        given(accountService.getAccountDetails(userId)).willReturn(Arrays.asList(dto));

        mockMvc.perform(get("/{userId}/account-detail", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountId").value(dto.accountId()))
                .andExpect(jsonPath("$[0].customerName").value(dto.customerName()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"CUSTOMER"})
    void getAccountDetailsWhenNoAccountsFound() {
        Long userId = 2L; // Assuming this user ID has no accounts
        // Mock the service to throw RuntimeException for this user ID
        given(accountService.getAccountDetails(userId))
                .willThrow(new RuntimeException("No accounts found for user ID: " + userId));

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/{userId}/account-detail", userId))
                    .andExpect(status().isNotFound()); // Expecting 404 Not Found status if properly handled
        });
    }


}
