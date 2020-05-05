Feature: Buying a product
  As a thirsty consumer
  I want to buy a product
  so that I can quench my thirst

  Background: 
    Given I'm at a vending machine
	
  Scenario: Buying a product without enough money should not dispense a product
    Given a vending machine with 0 cents
    When I buy a product    
    Then the vending machine should not dispense a product

  Scenario: Buying a product with enough money should dispense a product
    Given a vending machine with 50 cents
    When I buy a product    
    Then the vending machine should dispense a product
