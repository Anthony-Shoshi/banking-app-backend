package com.groupfour.bankingapp.stepdefinitions;

import com.groupfour.bankingapp.Models.Account;
import com.groupfour.bankingapp.Repository.AccountRepository;
import com.groupfour.bankingapp.Services.TransactionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class TransactionsStepDefinitions {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    private Account fromAccount;
    private Account toAccount;
    private String errorMessage;

    @Given("an account with IBAN {string} and balance {double} and daily limit {double}")
    public void an_account_with_iban_and_balance_and_daily_limit(String iban, double balance, double dailyLimit) {
        fromAccount = new Account();
        fromAccount.setIBAN(iban);
        fromAccount.setBalance(balance);
        fromAccount.setDailyLimit(dailyLimit);
        accountRepository.save(fromAccount);
    }

    @Given("an account with IBAN {string} and balance {double}")
    public void an_account_with_iban_and_balance(String iban, double balance) {
        toAccount = new Account();
        toAccount.setIBAN(iban);
        toAccount.setBalance(balance);
        accountRepository.save(toAccount);
    }

    @When("a transfer of amount {double} is made from {string} to {string}")
    public void a_transfer_of_amount_is_made_from_to(double amount, String fromIban, String toIban) {
        try {
            transactionService.transferMoney(fromIban, toIban, amount);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the transfer should be successful")
    public void the_transfer_should_be_successful() {
        Assertions.assertNull(errorMessage, "Expected no error message, but got: " + errorMessage);
    }

    @Then("the balance of {string} should be {double}")
    public void the_balance_of_should_be(String iban, double expectedBalance) {
        Account account = accountRepository.findByIBAN(iban);
        Assertions.assertEquals(expectedBalance, account.getBalance());
    }

    @Then("the transfer should fail with message {string}")
    public void the_transfer_should_fail_with_message(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, errorMessage);
    }
}
