package com.groupfour.bankingapp.stepdefinitions;

import com.groupfour.bankingapp.Controllers.CustomerTransactionController;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.TransactionRequestDTO;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Services.TransactionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerTransactionsStepDefinitions extends BaseStepDefinitions {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private CustomerTransactionController customerTransactionController;

    private ResponseEntity<Object> response;

    @Given("the customer has a valid account")
    public void the_customer_has_a_valid_account() {
        MockitoAnnotations.openMocks(this);
        // Assume valid account setup here
    }

    @When("the customer makes a deposit request with valid data")
    public void the_customer_makes_a_deposit_request_with_valid_data(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, 5.0);
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

        response = customerTransactionController.deposit(transactionRequest);
    }

    @Then("the system processes the deposit successfully")
    public void the_system_processes_the_deposit_successfully() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the response status is {int} OK")
    public void the_response_status_is_ok(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCodeValue());
    }

    @Then("the response contains the correct transaction details")
    public void the_response_contains_the_correct_transaction_details(String responseDetails) {
        BankTransactionDTO actualTransaction = (BankTransactionDTO) response.getBody();
        BankTransactionDTO expectedTransaction;

        if (actualTransaction.type() == TransactionType.DEPOSIT) {
            expectedTransaction = new BankTransactionDTO(
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
        } else {
            expectedTransaction = new BankTransactionDTO(
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
        }

        assertEquals(expectedTransaction, actualTransaction);
    }

    @When("the customer makes a deposit request with invalid data")
    public void the_customer_makes_a_deposit_request_with_invalid_data(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, -100.0);

        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid request"));

        response = customerTransactionController.deposit(transactionRequest);
    }

    @Then("the system responds with a bad request status")
    public void the_system_responds_with_a_bad_request_status() {
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Then("the response status is {int} Bad Request")
    public void the_response_status_is_bad_request(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCodeValue());
    }

    @Then("the response contains an error message {string}")
    public void the_response_contains_an_error_message(String message) {
        assertEquals(message, response.getBody());
    }

    @When("the customer makes a deposit request and an unexpected error occurs")
    public void the_customer_makes_a_deposit_request_and_an_unexpected_error_occurs(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, 100.0);

        when(transactionService.deposit(any(TransactionRequestDTO.class))).thenThrow(new RuntimeException());

        response = customerTransactionController.deposit(transactionRequest);
    }

    @Then("the system responds with an internal server error status")
    public void the_system_responds_with_an_internal_server_error_status() {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Then("the response status is {int} Internal Server Error")
    public void the_response_status_is_internal_server_error(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCodeValue());
    }

    @Then("the response contains a generic error message {string}")
    public void the_response_contains_a_generic_error_message(String message) {
        assertEquals(message, response.getBody());
    }

    @When("the customer makes a withdrawal request with valid data")
    public void the_customer_makes_a_withdrawal_request_with_valid_data(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, 1.0);
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

        response = customerTransactionController.withdraw(transactionRequest);
    }

    @Then("the system processes the withdrawal successfully")
    public void the_system_processes_the_withdrawal_successfully() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @When("the customer makes a withdrawal request with invalid data")
    public void the_customer_makes_a_withdrawal_request_with_invalid_data(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, -100.0);

        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid request"));

        response = customerTransactionController.withdraw(transactionRequest);
    }

    @When("the customer makes a withdrawal request and an unexpected error occurs")
    public void the_customer_makes_a_withdrawal_request_and_an_unexpected_error_occurs(String requestBody) {
        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(1L, 100.0);

        when(transactionService.withdraw(any(TransactionRequestDTO.class))).thenThrow(new RuntimeException());

        response = customerTransactionController.withdraw(transactionRequest);
    }
}
