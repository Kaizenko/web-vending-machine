
Feature: Inserting Coins
  As a thirsty consumer
  I want to insert coins
  so that I can buy a product

  Background: 
    Given I'm at a vending machine
	
  Scenario: Inserting 1 coin adds 25 cents to the total
    Given a vending machine with 0 cents
    When I insert a coin    
    Then the vending machine should have a total of 25 cents

  Scenario: Inserting 2 coins adds 50 cents to the total
    Given a vending machine with 50 cents
    When I insert a coin
    And I insert a coin    
    Then the vending machine should have a total of 100 cents    