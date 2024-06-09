package com.groupfour.bankingapp.Controllers;


import com.groupfour.bankingapp.Models.DTO.ApproveSignupPutDTO;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.CustomerService;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import com.groupfour.bankingapp.exception.InvalidLimitException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser
    void getCustomersWithoutAccountsReturnsEmptyList() throws Exception {
        Mockito.when(customerService.getCustomersWithoutAccounts()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/customers-without-accounts"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "Employee", roles = "EMPLOYEE") // Ensure this matches your global security configuration
    void approveSignup_UserNotFound() throws Exception, InvalidLimitException {
        Long userId = 1L;

        doThrow(new EntityNotFoundException("Customer not found"))
                .when(customerService).approveSignup(eq(userId), any(ApproveSignupPutDTO.class));

        mockMvc.perform(put("/employees/customers-without-accounts/" + userId + "/approve-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyLimit\":1000,\"absoluteLimitForCurrent\":500,\"absoluteLimitForSaving\":1500}")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(username = "Employee", roles = "EMPLOYEE")
    void approveSignup_Success() throws Exception, InvalidLimitException {
        Long userId = 1L;
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(1000, 500, 1500);

        doNothing().when(customerService).approveSignup(eq(userId), any(ApproveSignupPutDTO.class));

        mockMvc.perform(put("/employees/customers-without-accounts/" + userId + "/approve-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyLimit\":1000,\"absoluteLimitForCurrent\":500,\"absoluteLimitForSaving\":1500}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "Employee", roles = "EMPLOYEE")
    void approveSignup_InvalidLimitException() throws Exception, InvalidLimitException {
        Long userId = 1L;
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(-1000, -500, -1500); // Negative limits to trigger InvalidLimitException

        Mockito.doThrow(new InvalidLimitException("Invalid limit")).when(customerService).approveSignup(eq(userId), any(ApproveSignupPutDTO.class));

        mockMvc.perform(put("/employees/customers-without-accounts/" + userId + "/approve-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyLimit\":-1000,\"absoluteLimitForCurrent\":-500,\"absoluteLimitForSaving\":-1500}")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "Employee", roles = "EMPLOYEE")
    void approveSignup_RuntimeException() throws Exception, InvalidLimitException {
        Long userId = 1L;
        ApproveSignupPutDTO dto = new ApproveSignupPutDTO(1000, 500, 1500);

        doThrow(new RuntimeException("Internal server error")).when(customerService).approveSignup(eq(userId), any(ApproveSignupPutDTO.class));

        mockMvc.perform(put("/employees/customers-without-accounts/" + userId + "/approve-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dailyLimit\":1000,\"absoluteLimitForCurrent\":500,\"absoluteLimitForSaving\":1500}")
                        .with(csrf()))
                .andExpect(status().isInternalServerError());
    }


}
