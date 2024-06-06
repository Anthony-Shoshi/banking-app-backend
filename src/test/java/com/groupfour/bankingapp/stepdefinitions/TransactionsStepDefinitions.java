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
public class TransactionsStepDefinitions extends BaseStepDefinitions {



}
