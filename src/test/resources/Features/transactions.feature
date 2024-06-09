Feature: Getting all transactions

  Scenario: Getting all transactions
    Given I login as an employee
    When I request to get all transactions
    Then I should get all transactions
    Then I get a status code of 200

  Scenario: Getting a single transaction
    Given I login as an employee
    When I request to get a single transaction
    Then I should get a single transaction
    Then I get a status code of 200

  Scenario: Make a transfer
    Given I login as an employee
    When I request to post a transaction
    Then I get a status code of 200
    

