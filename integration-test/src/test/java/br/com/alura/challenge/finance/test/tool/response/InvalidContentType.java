package br.com.alura.challenge.finance.test.tool.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface InvalidContentType extends ResourceTest {
	
	ContentType invlaidContentType();

	@Override
	default RequestSpecification scene() {
		return RestAssured.given()
				.accept(ContentType.JSON)
                .contentType(invlaidContentType())
	            .body("");
	}

	@Test
	@Override
	@DisplayName("Should Content Type Invalid")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
				.statusCode(HttpStatus.SC_BAD_REQUEST)
	            .contentType(ContentType.JSON)
				.body("message", is("Media type Error"))
		        .body("details.size()", equalTo(1))
		        .body("details[0]", is("Content type 'text/plain;charset=ISO-8859-1' not supported"))
	        .extract()
	        	.asPrettyString();
		// @formatter:on
		assertThat(body).isNotEmpty();
	}

}
