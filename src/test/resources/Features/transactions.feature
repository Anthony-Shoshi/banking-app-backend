Feature: Getting all transactions

  Scenario: Getting all transactions
    Given an employee is logged in with role EMPLOYEE
    When request to get all transactions
    Then should get all transactions
    Then get a status code of 200

  Scenario: Getting transactions by Id
    Given an employee is logged in with role EMPLOYEE
    When request to get transactions of a customer with id 12345
    Then should get transactions of a customer
    Then get a status code of 200


  Scenario: Make a transfer
    Given an employee is logged in with role EMPLOYEE
    When request to post a transaction
    Then get a status code of 200


