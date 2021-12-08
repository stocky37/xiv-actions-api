package dev.stocky37.xiv.actions;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class JobsApiTest {
	@Test
	public void testJobEndpoint() {
		ValidatableResponse response = given()
			.when().get("/jobs/21")
			.then()
			.statusCode(200);
	}
}
