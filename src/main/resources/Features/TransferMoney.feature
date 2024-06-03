Feature: Transfer Money

  Scenario: Transfer money within daily limit
    Given an account with IBAN "fromAccountIban" and balance 1000 and daily limit 500
    And an account with IBAN "toAccountIban" and balance 500
    When a transfer of amount 100 is made from "fromAccountIban" to "toAccountIban"
    Then the transfer should be successful
    And the balance of "fromAccountIban" should be 900
    And the balance of "toAccountIban" should be 600

  Scenario: Transfer money exceeding daily limit
    Given an account with IBAN "fromAccountIban" and balance 1000 and daily limit 200
    And an account with IBAN "toAccountIban" and balance 500
    When a transfer of amount 300 is made from "fromAccountIban" to "toAccountIban"
    Then the transfer should fail with message "Transfer amount exceeds the daily limit"
