package br.com.alura.challenge.finance.test.tool.request;

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

public interface MethodNotSupported extends ResourceTest {
	
	@Override
	default RequestSpecification scene() {
		return RestAssured
                .given()
                    .body("");
	}

	@Test
	@Override
	@DisplayName("Should Method not supported")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse response) {
		// @formatter:off
		String body = response
				.statusCode(HttpStatus.SC_BAD_REQUEST)
	            .contentType(ContentType.JSON)
				.body("message", is("Request method"))
		        .body("details.size()", equalTo(1))
		        .body("details[0]", is("Request method '"+method()+"' not supported"))
	        .extract()
	        	.asPrettyString();
		// @formatter:on
		
		assertThat(body).isNotEmpty();
	}

}
