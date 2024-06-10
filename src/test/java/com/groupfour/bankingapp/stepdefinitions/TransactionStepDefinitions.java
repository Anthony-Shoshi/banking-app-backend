package com.groupfour.bankingapp.stepdefinitions;

import com.groupfour.bankingapp.Controllers.TransactionController;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Models.DTO.BankTransactionPostDTO;
import com.groupfour.bankingapp.Models.UserType;
import com.groupfour.bankingapp.Models.TransactionStatus;
import com.groupfour.bankingapp.Models.TransactionType;
import com.groupfour.bankingapp.Security.JwtTokenProvider;
import com.groupfour.bankingapp.Services.TransactionService;
import com.groupfour.bankingapp.Services.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionStepDefinitions extends BaseStepDefinitions {

    @Mock
    private TransactionService transactionService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionController transactionController;

    private ResponseEntity<Object> response;
    private List<BankTransactionDTO> expectedTransactions;

    @Given("an employee is logged in with role EMPLOYEE")
    public void an_employee_is_logged_in_with_role_EMPLOYEE() {
        MockitoAnnotations.openMocks(this);
        // Mock JWT token validation or any authentication logic if needed
        // For simplicity, let's assume the user is authenticated if the role is EMPLOYEE
    }

    @When("request to get all transactions")
    public void request_to_get_all_transactions() {
        // Define expected transactions
        expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.ROLE_EMPLOYEE,
                "John",
                "Doe",
                "DE89370400440532013000",
                "DE89370400440532013001",
                1000.00,
                "2023-06-10T10:15:30",
                TransactionStatus.SUCCESS
        ));
        expectedTransactions.add(new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.ROLE_EMPLOYEE,
                "Jane",
                "Smith",
                "DE89370400440532013002",
                "DE89370400440532013003",
                1500.00,
                "2023-06-10T11:00:00",
                TransactionStatus.FAILED
        ));

        when(transactionService.getAllTransactions()).thenReturn(expectedTransactions);
        response = transactionController.getAllTransactions();
    }

    @Then("should get all transactions")
    public void should_get_all_transactions() {
        // Cast response body to expected type
        List<BankTransactionDTO> actualTransactions = (List<BankTransactionDTO>) response.getBody();
        // Assert that the response body is the same as the expected transactions
        assertEquals(expectedTransactions, actualTransactions);
    }

    @Then("get a status code of {int}")
    public void get_a_status_code_of(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCodeValue());
    }

    @When("request to get transactions of a customer with id {long}")
    public void request_to_get_transactions_of_a_customer_with_id(Long customerId) {
        // Define expected transactions for a specific customer
        expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.ROLE_EMPLOYEE,
                "Alice",
                "Brown",
                "DE89370400440532013004",
                "DE89370400440532013005",
                2000.00,
                "2023-06-10T12:00:00",
                TransactionStatus.SUCCESS
        ));
        expectedTransactions.add(new BankTransactionDTO(
                TransactionType.TRANSFER,
                UserType.ROLE_EMPLOYEE,
                "Bob",
                "Green",
                "DE89370400440532013006",
                "DE89370400440532013007",
                2500.00,
                "2023-06-10T13:00:00",
                TransactionStatus.FAILED
        ));

        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(Optional.of(expectedTransactions));
        response = transactionController.getTransactionsByCustomerId(customerId);
    }

    @Then("should get transactions of a customer")
    public void should_get_transactions_of_a_customer() {
        // Cast response body to expected type
        List<BankTransactionDTO> actualTransactions = (List<BankTransactionDTO>) response.getBody();
        // Assert that the response body is the same as the expected transactions for the customer
        assertEquals(expectedTransactions, actualTransactions);
    }

    @When("request to post a transaction")
    public void request_to_post_a_transaction() {
        // Define a sample transaction request
        BankTransactionPostDTO transferRequest = new BankTransactionPostDTO(
                "DE89370400440532013000",
                "DE89370400440532013001",
                500.00
        );

        // Mock the service layer to return a response when the transaction is posted
        BankTransactionPostDTO expectedResponse = new BankTransactionPostDTO(
                "DE89370400440532013000",
                "DE89370400440532013001",
                500.00
        );
        when(transactionService.transferMoney(
                transferRequest.fromAccountIban(),
                transferRequest.toAccountIban(),
                transferRequest.transferAmount()
        )).thenReturn(expectedResponse);

        // Call the controller method
        response = transactionController.transferMoney(transferRequest, request);
    }
}
