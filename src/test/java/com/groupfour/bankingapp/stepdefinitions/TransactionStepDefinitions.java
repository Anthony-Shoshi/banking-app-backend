package com.groupfour.bankingapp.stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Models.TransactionStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionStepDefinitions extends BaseStepDefinitions{
    private static final String TRANSACTION_ENDPOINT = "/transactions";

    private static final String LOGIN_ENDPOINT = "/login";
    private final BankTransactionDTO  bankTransaction = new BankTransactionDTO(
                TransactionType.TRANSFER, // Replace with actual value
                UserType.ROLE_EMPLOYEE, // Replace with actual value
                "John",
                "Doe",
                "GB82WEST12345698765432",
                "GB82WEST12345698765433",
                1000.0,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                TransactionStatus.SUCCESS// Replace with actual value
            );

    private BankTransactionPostDTO transactionPostDTO;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private ResponseEntity<String> response;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    private LoginRequestDTO loginRequestDTO;
    @Given("I login as an employee")
    public void iLoginAsAn() throws JsonProcessingException {
        loginRequestDTO = new LoginRequestDTO("employee@example.com", "password123");

        String loginRequestJson = objectMapper.writeValueAsString(loginRequestDTO);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(loginRequestJson, httpHeaders);

        response = restTemplate.exchange(LOGIN_ENDPOINT, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            token = objectMapper.readTree(responseBody).get("token").asText();
            httpHeaders.set("Authorization", "Bearer " + token);
        } else {
            throw new RuntimeException("Failed to login: " + response.getStatusCode());
        }
    }

    @When("I request to get all transactions")
    public void i_request_to_get_all_transactions() {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, httpHeaders);
        response = restTemplate.exchange(TRANSACTION_ENDPOINT, HttpMethod.GET, request, String.class);
    }

    @Then("I should get all transactions")
    public void i_should_get_all_transactions() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("transactions")); // Adjust based on actual response structure
    }

    @Then("I get a status code of {int}")
    public void i_get_a_status_code_of(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCodeValue());
    }
}
