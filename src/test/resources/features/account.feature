Feature: Account Management

  Scenario: Employee retrieves all customer accounts
    Given an employee is logged in with role EMPLOYEE
    When the employee requests all customer accounts
    Then the system should return all accounts with status 200

  Scenario: Employee retrieves no customer accounts
    Given an employee is logged in with role EMPLOYEE
    When the employee requests all customer accounts
    Then the system should return an empty list with status 200

  Scenario: Customer retrieves their account details successfully
    Given a customer is logged in with role CUSTOMER
    And the customer has an account
    When the customer requests their account details
    Then the system should return the customer's account details with status 200

  Scenario: Customer retrieves account details with no accounts
    Given a customer is logged in with role CUSTOMER
    When the customer requests their account details
    Then the system should return a not found status 404
