package br.com.alura.challenge.finance.test.integration.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface BusinessExceptionTest extends ResourceTest {

	Logger log = LoggerFactory.getLogger(BusinessExceptionTest.class);

	String body();

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON).body(body());
	}

	@Test
	@Override
	@DisplayName("Should create")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("message", is("Error business"))
                .body("details.size()", equalTo(1))
                .body("details[0]", is("There is this finance for this month"))
            .extract()
            	.asString();
		// @formatter:on

		log.debug(body);

		assertThat(body).isNotEmpty();
	}

}
