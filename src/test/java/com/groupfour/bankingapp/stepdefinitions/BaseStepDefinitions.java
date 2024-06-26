package com.groupfour.bankingapp.stepdefinitions;

//import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@AutoConfigureMockMvc
public class BaseStepDefinitions {
}
