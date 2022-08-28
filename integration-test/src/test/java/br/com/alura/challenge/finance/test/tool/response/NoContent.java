package br.com.alura.challenge.finance.test.tool.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface NoContent extends ResourceTest {

	Logger log = LoggerFactory.getLogger(NoContent.class);

	@Override
	default RequestSpecification scene() {
		return given().accept(ContentType.JSON);
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
                    .asString();
		// @formatter:on

		log.debug(body);

		assertThat(body).isEmpty();
	}

}
