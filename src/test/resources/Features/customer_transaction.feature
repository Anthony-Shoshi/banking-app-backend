#Feature: Customer Transaction API Tests
#
#  Scenario: Get Transaction History
#    Given I am an authenticated customer
#    When I request the transaction history with customerId 1, startDate "2024-06-01", endDate "2024-06-30", fromAmount 1, toAmount 900, iban "DE89 3704 0044 0532 0130 14"
#    Then I should receive the following transactions:
#      | type    | initiatedBy | firstName | lastName | fromAccountIban              | toAccountIban                | transferAmount | currentTime | status  |
#      | DEPOSIT | ROLE_CUSTOMER    | Ador      | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 00  | 25.0           | 2024-06-07  | SUCCESS |
#      | WITHDRAW| ROLE_CUSTOMER    | Ador      | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 14  | 20.0           | 2024-06-07  | SUCCESS |
#
#  Scenario: Get IBAN by Customer Name
#    Given I am an authenticated employee
#    When I search for the IBAN with firstName "Ador" and lastName "Negash"
#    Then I should receive the IBAN "DE89 3704 0044 0532 0130 14"
#
#  Scenario: Get Accounts by User ID
#    Given I am an authenticated customer
#    When I request the accounts with userId 1
#    Then I should receive the following accounts:
#      | accountId | customerId | customerName | IBAN                      | balance | accountType | status   | absoluteLimit | dailyLimit |
#      | 1         | 1          | Ador Negash  | DE89 3704 0044 0532 0130 14 | 91.0    | CURRENT     | APPROVED | 0.0           | 50.0       |
#      | 2         | 1          | Ador Negash  | DE89 3704 0044 0532 0130 00 | 109.0   | SAVING      | APPROVED | 0.0           | 50.0       |
Feature: Customer Transaction Management

  Scenario: Successful Deposit
    Given the customer has a valid account
    When the customer makes a deposit request with valid data
      """
      {
        "accountId": 1,
        "amount": 5.0
      }
      """
    Then the system processes the deposit successfully
    And the response status is 200 OK
    And the response contains the correct transaction details
      """
      {
        "type": "DEPOSIT",
        "initiatedBy": "ROLE_CUSTOMER",
        "firstName": "Ador",
        "lastName": "Negash",
        "fromAccountIban": "DE89 3704 0044 0532 0130 14",
        "toAccountIban": null,
        "transferAmount": 5.0,
        "currentTime": "2024-06-01T04:33:58.821569900",
        "status": "SUCCESS"
      }
      """

  Scenario: Deposit with Invalid Data
    Given the customer has a valid account
    When the customer makes a deposit request with invalid data
      """
      {
        "accountId": 1,
        "amount": -100.0
      }
      """
    Then the system responds with a bad request status
    And the response status is 400 Bad Request
    And the response contains an error message "Invalid request"

  Scenario: Internal Server Error during Deposit
    Given the customer has a valid account
    When the customer makes a deposit request and an unexpected error occurs
      """
      {
        "accountId": 1,
        "amount": 100.0
      }
      """
    Then the system responds with an internal server error status
    And the response status is 500 Internal Server Error
    And the response contains a generic error message "An unexpected error occurred"

  Scenario: Successful Withdrawal
    Given the customer has a valid account
    When the customer makes a withdrawal request with valid data
      """
      {
        "accountId": 1,
        "amount": 1.0
      }
      """
    Then the system processes the withdrawal successfully
    And the response status is 200 OK
    And the response contains the correct transaction details
      """
      {
        "type": "WITHDRAW",
        "initiatedBy": "ROLE_CUSTOMER",
        "firstName": "Ador",
        "lastName": "Negash",
        "fromAccountIban": "DE89 3704 0044 0532 0130 14",
        "toAccountIban": null,
        "transferAmount": 1.0,
        "currentTime": "2024-06-04T22:37:57.047884",
        "status": "SUCCESS"
      }
      """

  Scenario: Withdrawal with Invalid Data
    Given the customer has a valid account
    When the customer makes a withdrawal request with invalid data
      """
      {
        "accountId": 1,
        "amount": -100.0
      }
      """
    Then the system responds with a bad request status
    And the response status is 400 Bad Request
    And the response contains an error message "Invalid request"

  Scenario: Internal Server Error during Withdrawal
    Given the customer has a valid account
    When the customer makes a withdrawal request and an unexpected error occurs
      """
      {
        "accountId": 1,
        "amount": 100.0
      }
      """
    Then the system responds with an internal server error status
    And the response status is 500 Internal Server Error
    And the response contains a generic error message "An unexpected error occurred"
