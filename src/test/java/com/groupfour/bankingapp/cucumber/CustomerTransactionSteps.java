//package com.groupfour.bankingapp.cucumber;
//
//import com.groupfour.bankingapp.Models.DTO.AccountsGetDTO;
//import com.groupfour.bankingapp.Models.DTO.BankTransactionDTO;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@SpringBootTest
//@ContextConfiguration
//public class CustomerTransactionSteps {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private ResultActions resultActions;
//
//    @Given("I am an authenticated customer")
//    public void iAmAnAuthenticatedCustomer() {
//        // Add logic to authenticate the customer and set up the mockMvc with the necessary authentication
//        mockMvc = MockMvcBuilders.standaloneSetup(new YourController())
//                .defaultRequest(get("/")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer your_token_here")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .build();
//    }
//
//    @When("I request the transaction history with customerId {long}, startDate {string}, endDate {string}, fromAmount {double}, toAmount {double}, iban {string}")
//    public void iRequestTheTransactionHistory(Long customerId, String startDate, String endDate, Double fromAmount, Double toAmount, String iban) throws Exception {
//        resultActions = mockMvc.perform(get("/customers/transaction-history")
//                .param("customerId", customerId.toString())
//                .param("startDate", startDate)
//                .param("endDate", endDate)
//                .param("fromAmount", fromAmount.toString())
//                .param("toAmount", toAmount.toString())
//                .param("iban", iban));
//    }
//
//    @Then("I should receive the following transactions:")
//    public void iShouldReceiveTheFollowingTransactions(List<BankTransactionDTO> expectedTransactions) throws Exception {
//        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
//        List<BankTransactionDTO> actualTransactions = // Parse jsonResponse to List<BankTransactionDTO>
//                assertThat(actualTransactions).isEqualTo(expectedTransactions);
//    }
//
//    @When("I search for the IBAN with firstName {string} and lastName {string}")
//    public void iSearchForTheIBANWithFirstNameAndLastName(String firstName, String lastName) throws Exception {
//        resultActions = mockMvc.perform(get("/customers/search-iban")
//                .param("firstName", firstName)
//                .param("lastName", lastName));
//    }
//
//    @Then("I should receive the IBAN {string}")
//    public void iShouldReceiveTheIBAN(String expectedIban) throws Exception {
//        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
//        String actualIban = // Parse jsonResponse to extract IBAN
//                assertThat(actualIban).isEqualTo(expectedIban);
//    }
//
//    @When("I request the accounts with userId {long}")
//    public void iRequestTheAccountsWithUserId(Long userId) throws Exception {
//        resultActions = mockMvc.perform(get("/accounts/" + userId));
//    }
//
//    @Then("I should receive the following accounts:")
//    public void iShouldReceiveTheFollowingAccounts(List<AccountsGetDTO> expectedAccounts) throws Exception {
//        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();
//        List<AccountsGetDTO> actualAccounts = // Parse jsonResponse to List<AccountsGetDTO>
//                assertThat(actualAccounts).isEqualTo(expectedAccounts);
//    }
//}