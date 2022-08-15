package br.com.alura.challenge.finance.test.tool.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface NoContent extends ResourceTest {

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON);
	}

	@Test
	@Override
	@DisplayName("Should response no content")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
				.statusCode(HttpStatus.SC_NO_CONTENT)
                .extract()
                    .asPrettyString();
		// @formatter:on
		assertThat(body).isEmpty();
	}

}
