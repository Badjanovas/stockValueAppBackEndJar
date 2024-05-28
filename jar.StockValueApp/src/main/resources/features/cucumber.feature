Feature: Calculate Graham's Valuation

  Scenario: Calculate Graham's Valuation with valid input
    Given the EPS is 10.0
    And the growth rate is 5.0
    And the base PE is 7.0
    And the average yield of AAA bonds is 4.4
    And the current yield of bonds is 5.09
    When I calculate Graham's valuation
    Then the result should be 103.73