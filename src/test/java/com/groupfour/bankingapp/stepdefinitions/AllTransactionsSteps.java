package com.groupfour.bankingapp.stepdefinitions;

import com.groupfour.bankingapp.Models.*;
import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
import com.groupfour.bankingapp.Repository.TransactionRepository;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Repository.UserRepository;
import com.groupfour.bankingapp.Services.TransactionService;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AllTransactionsSteps extends BaseStepDefinitions{

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<List<BankTransactionDTO>> response;

    private static final Logger logger = Logger.getLogger(AllTransactionsSteps.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String getCurrentTime() {
        return LocalDateTime.now().format(formatter);
    }

    @DataTableType
    public BankTransactionDTO defineBankTransactionDTO(Map<String, String> entry) {
        return new BankTransactionDTO(
                TransactionType.valueOf(entry.get("type")),
                UserType.valueOf(entry.get("initiatedBy")),
                entry.get("firstName"),
                entry.get("lastName"),
                entry.get("fromAccountIban"),
                entry.get("toAccountIban"),
                Double.valueOf(entry.get("transferAmount")),
                entry.getOrDefault("currentTime", getCurrentTime()),
                TransactionStatus.valueOf(entry.get("status"))
        );
    }

    @Before
    public void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Given("the following bank transactions exist:")
    public void the_following_bank_transactions_exist(List<BankTransactionDTO> transactions) {
        logger.info("Saving accounts and users");
        transactions.forEach(dto -> {

            Account fromAccount = accountRepository.findByIBAN(dto.fromAccountIban());
            if (fromAccount == null) {
                fromAccount = new Account(null, dto.fromAccountIban(), 0.0, 0.0, null, true, 0.0, AccountStatus.ACTIVE, "EUR");
                accountRepository.save(fromAccount);
            }

            Account toAccount = accountRepository.findByIBAN(dto.toAccountIban());
            if (toAccount == null) {
                toAccount = new Account(null, dto.toAccountIban(), 0.0, 0.0, null, true, 0.0, AccountStatus.ACTIVE, "EUR");
                accountRepository.save(toAccount);
            }

            User user = new User();
            user.setFirstName(dto.firstName());
            user.setLastName(dto.lastName());
            user.setRole(dto.initiatedBy());
            user.setDateOFbirth("1990-01-01"); // Set a default date of birth
            user.setEmail(dto.firstName() + "." + dto.lastName() + "@example.com");
            user.setPassword("password");
            user.setBsn(UUID.randomUUID().toString());
            user.setGender(Gender.MALE);
            user.setPhoneNumber("1234567890");
            userRepository.save(user);
        });

        logger.info("Saving transactions");
        List<BankTransaction> bankTransactions = transactions.stream().map(dto -> {
            LocalDateTime dateTime = LocalDateTime.parse(dto.currentTime(), formatter);
            Account fromAccount = accountRepository.findByIBAN(dto.fromAccountIban());
            Account toAccount = accountRepository.findByIBAN(dto.toAccountIban());
            List<User> users = userRepository.findByFirstName(dto.firstName());

            // Assuming you want to pick the first user if multiple users are found
            User user = users.isEmpty() ? null : users.get(0);

            return new BankTransaction(
                    dto.type(),
                    dto.initiatedBy(),
                    user,
                    fromAccount,
                    toAccount,
                    dto.transferAmount(),
                    dateTime,
                    dto.status()
            );
        }).collect(Collectors.toList());

        transactionRepository.saveAll(bankTransactions);

        // Log the saved transactions for verification
        logger.info("Saved transactions: " + transactionRepository.findAll().toString());
    }

    @When("I request to retrieve all transactions")
    public void i_request_to_retrieve_all_transactions() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/transactions"; // Ensure your server is running on this port

        ParameterizedTypeReference<List<BankTransactionDTO>> responseType = new ParameterizedTypeReference<List<BankTransactionDTO>>() {};
        ResponseEntity<List<BankTransactionDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        response = responseEntity;
    }

    @Then("I should receive a list of transactions")
    public void i_should_receive_a_list_of_transactions() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Then("the list should contain the following transactions:")
    public void the_list_should_contain_the_following_transactions(List<BankTransactionDTO> expectedTransactions) {
        List<BankTransactionDTO> actualTransactions = response.getBody();
        assertNotNull(actualTransactions, "Response body is null");
        assertEquals(expectedTransactions.size(), actualTransactions.size(), "Mismatch in the number of transactions");

        Duration tolerance = Duration.ofMinutes(1);  // Set a tolerance of 1 minute

        for (int i = 0; i < expectedTransactions.size(); i++) {
            BankTransactionDTO expected = expectedTransactions.get(i);
            BankTransactionDTO actual = actualTransactions.get(i);

            assertEquals(expected.type(), actual.type());
            assertEquals(expected.initiatedBy(), actual.initiatedBy());
            assertEquals(expected.firstName(), actual.firstName());
            assertEquals(expected.lastName(), actual.lastName());
            assertEquals(expected.fromAccountIban(), actual.fromAccountIban());
            assertEquals(expected.toAccountIban(), actual.toAccountIban());
            assertEquals(expected.transferAmount(), actual.transferAmount());

            LocalDateTime expectedTime = LocalDateTime.parse(expected.currentTime(), formatter);
            LocalDateTime actualTime = LocalDateTime.parse(actual.currentTime(), formatter);
            Duration difference = Duration.between(expectedTime, actualTime).abs();

            logger.info(String.format("Expected time: %s, Actual time: %s, Difference: %s seconds",
                    expectedTime, actualTime, difference.getSeconds()));

            assertTrue(difference.compareTo(tolerance) <= 0, String.format("Timestamps do not match within tolerance. Expected: %s, Actual: %s, Difference: %s seconds", expectedTime, actualTime, difference.getSeconds()));
            assertEquals(expected.status(), actual.status());
        }
    }
}
