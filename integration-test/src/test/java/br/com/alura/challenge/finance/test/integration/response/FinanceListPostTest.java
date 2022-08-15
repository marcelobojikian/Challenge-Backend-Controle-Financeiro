package br.com.alura.challenge.finance.test.integration.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.test.tool.ResourceTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface FinanceListPostTest<T extends FinanceEntity> extends ResourceTest {
	
	void each(T entity) throws JsonProcessingException;

	@Override
	default RequestSpecification scene() {
		return RestAssured.given()
				.accept(ContentType.JSON)
                .contentType(ContentType.JSON);
	}
	
	@Override
	default String method() {
		return ResourceTest.POST;
	}

	default void run() {
	}

	default void run(String body) {
		
		RequestSpecification scene =scene().body(body).when();

		ValidatableResponse reponse = method(scene).then();

    	asserts(reponse); 
    	
	}

	@Override
	default void asserts(ValidatableResponse reponse) {
		// @formatter:off
		String body = reponse
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .body("_links.self.href", not(empty()))
                .body("_links.incomes.href", not(empty()))
            .extract()
            	.asPrettyString();
		// @formatter:on
		assertThat(body).isNotEmpty();
	}

}
