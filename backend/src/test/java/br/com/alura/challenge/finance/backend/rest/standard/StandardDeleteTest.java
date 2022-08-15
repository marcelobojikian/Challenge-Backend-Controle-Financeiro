package br.com.alura.challenge.finance.backend.rest.standard;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.backend.rest.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface StandardDeleteTest extends ResourceTest {

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON);
	}
	
	@Override
	default String method() {
		return ResourceTest.DELETE;
	}

	@Test
	@Override
	@DisplayName("Should delete")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_OK)
            .extract()
            	.asPrettyString();
		// @formatter:on
		assertThat(body).isEmpty();
	}

}
