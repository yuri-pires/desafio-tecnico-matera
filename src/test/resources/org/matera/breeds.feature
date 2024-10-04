Feature: GET /breeds endpoint

  Scenario: Get a list of 1 breed
    Given I have access to the Cat Facts API
    When I send a GET request to the breeds endpoint with a limit of 1 for the breeds list
    Then I have a list of breeds

  Scenario: Get a list of various breeds
    Given I have access to the Cat Facts API
    When I send a GET request to the breeds endpoint with a limit of 6 for the breeds list
    Then I have a list of breeds
