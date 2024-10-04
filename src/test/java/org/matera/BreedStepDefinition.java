package org.matera;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BreedStepDefinition {
  private Response response;
  private boolean isConnectionClosed;

  @Given("I have access to the Cat Facts API")
  public void iHaveAccessToTheCatFactAPi() {
    baseURI = "https://catfact.ninja";
  }

  @When("I send a GET request to the breeds endpoint with a limit of {int} for the breeds list")
  public void iSendAGetRequestToTheBreedsEndpointWithLimitParameter(Integer limitQueryValue) {
    response =
      given()
        .param("limit", limitQueryValue)
        .header("accept", "application/json")
      .when()
        .get("/breeds");
  }

  /**
   * Teste de segurança que valida o comportamento do servidor ao receber um Header "Host" inválido.
   * Usa um bloco {@code try-catch} para capturar exceções de conexão, já que o servidor pode fechar a conexão,
   * o que resultaria em uma falha no Java. O objetivo é garantir que o servidor responda corretamente fechando a
   * conexão, protegendo contra ataques de manipulação do cabeçalho "Host".
   */
  @When("I send a GET request to the breeds endpoint with invalid Host header")
  public void iSendAGetRequestToTheBreedsEndpointWithInvalidHostHeader() {
    try {
      response =
        given()
          .param("limit", 1)
          .header("accept", "application/json")
          .header("Host", "api.fake.com")
        .when()
          .get("/breeds");

      isConnectionClosed = false;
    } catch (Exception e) {
      System.out.println("org.apache.http.NoHttpResponseException handle this request -> " + e);
      isConnectionClosed = true;
    }
  }

  @When("I send a POST request instead of a GET request to the breeds endpoint")
  public void iSendAPOSTRequestInsteadOfAGETRequestToTheBreedsEndpoint() {
    response =
      given()
        .param("limit", 1)
        .header("accept", "application/json")
      .when()
        .post("/breeds");
  }

  @Then("I should receive a connection close from the server")
  public void iShouldReceiveAConnectionCloseFromTheServer() {
    assertTrue(isConnectionClosed);
  }

  @Then("I should receive a status code of {int}")
  public void iShouldReceiveAStatusCodeOf(int statusCode) {
    response.then()
      .statusCode(statusCode);
  }

  @And("I should have a list of breeds containing exactly {int} item(s)")
  public void iShouldHaveAListOfBreedsContainingExactlyItem(int expectedBreedArrayLenght) {
    response.then()
      .body("current_page", equalTo(1))
      .body("data", hasSize(expectedBreedArrayLenght));
  }

  @And("each breed object should contain the following fields - breed, country, origin, coat, pattern")
  public void eachBreedObjectShouldContainTheFollowingFields() {
    response.then()
      .body("data", everyItem(
        allOf(
          hasKey("breed"),
          hasKey("country"),
          hasKey("origin"),
          hasKey("coat"),
          hasKey("pattern")
        )
      ));
  }

  @And("I should receive a message with the reason Not Found")
  public void iShouldReceiveAMessageWithTheReasonNotFound(){
    response.then()
      .body("message", equalTo("Not Found"))
      .body("code", equalTo(404));
  }
}
