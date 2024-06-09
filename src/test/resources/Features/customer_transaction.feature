Feature: Customer Transaction API Tests

  Scenario: Get Transaction History
    Given I am an authenticated customer
    When I request the transaction history with customerId 1, startDate "2024-06-01", endDate "2024-06-30", fromAmount 1, toAmount 900, iban "DE89 3704 0044 0532 0130 14"
    Then I should receive the following transactions:
      | type    | initiatedBy | firstName | lastName | fromAccountIban              | toAccountIban                | transferAmount | currentTime | status  |
      | DEPOSIT | ROLE_CUSTOMER    | Ador      | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 00  | 25.0           | 2024-06-07  | SUCCESS |
      | WITHDRAW| ROLE_CUSTOMER    | Ador      | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 14  | 20.0           | 2024-06-07  | SUCCESS |

  Scenario: Get IBAN by Customer Name
    Given I am an authenticated employee
    When I search for the IBAN with firstName "Ador" and lastName "Negash"
    Then I should receive the IBAN "DE89 3704 0044 0532 0130 14"

  Scenario: Get Accounts by User ID
    Given I am an authenticated customer
    When I request the accounts with userId 1
    Then I should receive the following accounts:
      | accountId | customerId | customerName | IBAN                      | balance | accountType | status   | absoluteLimit | dailyLimit |
      | 1         | 1          | Ador Negash  | DE89 3704 0044 0532 0130 14 | 91.0    | CURRENT     | APPROVED | 0.0           | 50.0       |
      | 2         | 1          | Ador Negash  | DE89 3704 0044 0532 0130 00 | 109.0   | SAVING      | APPROVED | 0.0           | 50.0       |
