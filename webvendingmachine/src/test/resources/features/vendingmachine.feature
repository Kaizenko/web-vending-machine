Feature: VendingMachine 

  Scenario: Making a payment increments by 25
    When at the "vendingmachine"
    And I make a payment
    Then the amount paid is the initial amount plus 25

  Scenario: Making a default payment sets amount paid to 25
    When at the "vendingmachine"
    And I make a payment
    Then the amount paid is 25 
    
  Scenario: Release change resets payment to 0
    When at the "vendingmachine"
    And I make a payment
    And I release change
    Then the amount paid is 0
    
  Scenario: Making a payment twice increments by 50
    When at the "vendingmachine"
    And I make a payment
    And I make a payment
    Then the amount paid is the initial amount plus 50  
    
  Scenario: Release change with payment fo 25 returns 25 for change
    When at the "vendingmachine"
    And I make a payment
    And I release change
    Then the change released is 25
    
  Scenario: Making 2 payments sets amount paid to 50
    When at the "vendingmachine"
    And I make a payment
    And I make a payment
    Then the amount paid is 50
    
  Scenario: Release change without payment returns 0 change
    When at the "vendingmachine"    
    And I release change
    Then the change released is 0
    
 
