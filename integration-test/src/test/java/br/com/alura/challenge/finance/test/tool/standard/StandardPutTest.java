package br.com.alura.challenge.finance.test.tool.standard;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface StandardPutTest extends ResourceTest {
	
	String body();

	@Override
	default RequestSpecification scene() {
		return RestAssured.given()
				.accept(ContentType.JSON)
                .contentType(ContentType.JSON)
	            .body(body());
	}
	
	@Override
	default String method() {
		return ResourceTest.PUT;
	}

	@Test
	@Override
	@DisplayName("Should update")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
            .extract()
            	.asPrettyString();
		// @formatter:on
		assertThat(body).isNotEmpty();
	}

}
