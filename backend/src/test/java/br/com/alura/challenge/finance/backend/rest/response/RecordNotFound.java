package br.com.alura.challenge.finance.backend.rest.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.backend.rest.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface RecordNotFound extends ResourceTest {

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON);
	}

	@Test
	@Override
	@DisplayName("Should Record not found")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
            	.statusCode(HttpStatus.SC_NOT_FOUND)
		        .body("message", is("Record not found"))
		        .body("details.size()", equalTo(1))
		        .body("details[0]", is("Entity not found"))
            .extract()
                .asPrettyString();
		// @formatter:on
		assertThat(body).isNotEmpty();
	}

}
