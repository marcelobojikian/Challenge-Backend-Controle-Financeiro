package br.com.alura.challenge.finance.test.tool.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface SingleHateoasFormat extends ResourceTest {

	String getResource();

	String collectionRelationName();

	@Override
	default RequestSpecification scene() {
		return RestAssured.given().accept(ContentType.JSON);
	}

	@Test
	@Override
	@DisplayName("Should have Hateoas format")
	default void run() {
		ResourceTest.super.run();
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body(linkSelfHref(), not(empty()))
                .body(linkListHref(), not(empty()))
            .extract()
            	.asPrettyString();
		// @formatter:on
		assertThat(body).isNotEmpty();
	}

	default String linkSelfHref() {
		return "_links.self.href";
	}

	default String linkListHref() {
		return "_links." + collectionRelationName() + ".href";
	}
}
