package br.com.alura.challenge.finance.test.tool.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface InvalidContentType extends ResourceTest {

	Logger log = LoggerFactory.getLogger(InvalidContentType.class);

	ContentType invlaidContentType();

	@Override
	default RequestSpecification scene() {
		return given().accept(ContentType.JSON).contentType(invlaidContentType()).body("");
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
				.statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
	            .contentType(ContentType.JSON)
				.body("message", is("Content type '"+invlaidContentType()+";charset=ISO-8859-1' not supported"))
		        .body("details.size()", equalTo(1))
		        .body("details[0]", is(invlaidContentType()+";charset=ISO-8859-1 media type is not supported. Supported media types are application/json application/*+json"))
	        .extract()
        		.asString();
		// @formatter:on

		log.debug(body);

		assertThat(body).isNotEmpty();
	}

}
