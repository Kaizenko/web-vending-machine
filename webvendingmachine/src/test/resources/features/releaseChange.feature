
Feature: Releasing Change
  As a thirsty consumer
  I want to release change
  so that I can get my money back

  Background: 
    Given I'm at a vending machine
	
  Scenario: Releasing change without inserting money returns 0
    Given a vending machine with 0 cents
    When I release change    
    Then the vending machine should release 0 cents

  Scenario: Releasing change after inserting money returns change
    Given a vending machine with 25 cents
    When I release change    
    Then the vending machine should release 25 cents
    
  Scenario: Releasing change after inserting more money returns all change
    Given a vending machine with 75 cents
    When I release change    
    Then the vending machine should release 75 cents

  Scenario: Releasing change after buying product with exact money returns no change
    Given a vending machine with 50 cents
    And I buy a product
    When I release change    
    Then the vending machine should release 0 cents
    
  Scenario: Releasing change after buying product with extra money returns change
    Given a vending machine with 75 cents
    And I buy a product
    When I release change    
    Then the vending machine should release 25 cents
    