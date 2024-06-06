Feature: Retrieve All Transactions
  As an employee of the banking app
  I want to retrieve all bank transactions
  So that I can view the transaction history

  Scenario: Successfully retrieve all transactions
    Given the following bank transactions exist:
      | type     | initiatedBy | firstName | lastName | fromAccountIban | toAccountIban | transferAmount | currentTime       | status  |
      | DEPOSIT  | CUSTOMER    | Ador    | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 00 | 25.00         | 2023-06-01 10:30  | SUCCESS |
      | DEPOSIT  | CUSTOMER    | Fateme    | Sabagh   | DE89 3704 0044 0532 0130 12 | DE89 3704 0044 0532 0130 11 | 1000.00       | 2023-06-01 10:30  | SUCCESS |
    When I request to retrieve all transactions
    Then I should receive a list of transactions
    And the list should contain the following transactions:
      | type     | initiatedBy | firstName | lastName | fromAccountIban | toAccountIban | transferAmount | currentTime       | status  |
      | DEPOSIT  | CUSTOMER    | Ador      | Negash   | DE89 3704 0044 0532 0130 14 | DE89 3704 0044 0532 0130 00 | 25.00         | 2023-06-01 10:30  | SUCCESS |
      | DEPOSIT  | CUSTOMER    | Fateme    | Sabagh   | DE89 3704 0044 0532 0130 12 | DE89 3704 0044 0532 0130 11 | 1000.00       | 2023-06-01 10:30  | SUCCESS |
