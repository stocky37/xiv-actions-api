package dev.stocky37.xiv.actions;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class JobsApiTest {
	@Test
	public void testHelloEndpoint() {
		ValidatableResponse response = given()
			.when().get("/jobs")
			.then()
			.statusCode(200);

		response.body("[0].actions", Matchers.nullValue());
	}
}
