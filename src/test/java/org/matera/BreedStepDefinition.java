package org.matera;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BreedStepDefinition {
  private Response response;
  private Integer limitQueryParamLength;

  @Given("I have access to the Cat Facts API")
  public void base_url_to_cat_facts_api() {
    baseURI = "https://catfact.ninja";
  }

  @When("I send a GET request to the breeds endpoint with a limit of {int} for the breeds list")
  public void send_request_to_breeds_endpoint(Integer limitQueryValue) {
    limitQueryParamLength = limitQueryValue;

    response =
      given()
        .param("limit", limitQueryParamLength)
        .header("accept", "application/json")
      .when()
        .get("/breeds");
  }

  @Then("I have a list of breeds")
  public void i_get_a_list_of_breeds() {
    response.then()
      .statusCode(200)
      .body("current_page", equalTo(1))
      .body("data", hasSize(limitQueryParamLength))
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
