package com.groupfour.bankingapp.cucumber;

import com.groupfour.bankingapp.stepdefinitions.BaseStepDefinitions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@WithMockUser(username="employee", roles={"EMPLOYEE"})
public class AccountStepDefi extends BaseStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Given("an employee is logged in with role EMPLOYEEE")
    public void an_employee_is_logged_in_with_role_EMPLOYEEE() {
        // Mocking security context here or using Spring Security Test

    }

    @When("the employee requests all customer accounts")
    public void the_employee_requests_all_customer_accounts() throws Exception {
        mockMvc.perform(get("/employees/customer-accounts")).andExpect(status().isOk());
    }

    @Then("the system should return all accounts with status 200")
    public void the_system_should_return_all_accounts_with_status_200() throws Exception {
        mockMvc.perform(get("/employees/customer-accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Then("the system should return an empty list with status 200")
    public void the_system_should_return_an_empty_list_with_status_200() throws Exception {
        mockMvc.perform(get("/employees/customer-accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Given("a customer is logged in with role CUSTOMER")
    public void aCustomerIsLoggedInWithRoleCUSTOMER() {
    }

    @And("the customer has an account")
    public void theCustomerHasAnAccount() {
    }

    @When("the customer requests their account details")
    public void theCustomerRequestsTheirAccountDetails() {
    }

    @Then("the system should return the customer's account details with status {int}")
    public void theSystemShouldReturnTheCustomerSAccountDetailsWithStatus(int arg0) {
    }

    @Then("the system should return a not found status {int}")
    public void theSystemShouldReturnANotFoundStatus(int arg0) {
    }

    // Add other steps similarly, considering the role and the possible results.
}

