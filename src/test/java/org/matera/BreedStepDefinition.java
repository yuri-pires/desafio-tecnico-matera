package org.matera;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BreedStepDefinition {
  private Response response;

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
}
