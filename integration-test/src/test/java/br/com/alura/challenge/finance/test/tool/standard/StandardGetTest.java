package br.com.alura.challenge.finance.test.tool.standard;

import static org.assertj.core.api.Assertions.assertThat;

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

public interface StandardGetTest extends ResourceTest {

	Logger log = LoggerFactory.getLogger(StandardGetTest.class);

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON);
	}

	@Test
	@Override
	@DisplayName("Should get")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_OK)
            .extract()
            	.asString();
		// @formatter:on

		log.debug(body);

		assertThat(body).isEmpty();
	}

}
