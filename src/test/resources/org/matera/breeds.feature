Feature: GET /breeds endpoint

  Scenario: Get a list of 1 breed
    Given I have access to the Cat Facts API
    When I send a GET request to the breeds endpoint with a limit of 1 for the breeds list
    Then I should receive a status code of 200
    And I should have a list of breeds containing exactly 1 item
    And each breed object should contain the following fields - breed, country, origin, coat, pattern

  Scenario: Get a list of various breeds
    Given I have access to the Cat Facts API
    When I send a GET request to the breeds endpoint with a limit of 6 for the breeds list
    Then I should receive a status code of 200
    And I should have a list of breeds containing exactly 6 items
    And each breed object should contain the following fields - breed, country, origin, coat, pattern

  Scenario: Handling invalid method request to the breeds endpoint
    Given I have access to the Cat Facts API
    When I send a POST request instead of a GET request to the breeds endpoint
    Then I should receive a status code of 404
    And I should receive a message with the reason Not Found
    
  Scenario: Handling invalid Host header in GET request to breeds endpoint
    Given I have access to the Cat Facts API
    When I send a GET request to the breeds endpoint with invalid Host header
    And I should receive a connection close from the server