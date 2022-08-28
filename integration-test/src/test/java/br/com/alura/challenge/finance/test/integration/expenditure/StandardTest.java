package br.com.alura.challenge.finance.test.integration.expenditure;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.test.integration.AuthenticatedAccess;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WithMockUser(username = "user")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { ControleFinanceiroApplication.class })
@DisplayName("Expenditure Standard Test")
public class StandardTest extends AuthenticatedAccess {
	
	static final String RESOURCE = "/api/expenditures";

	@Nested
	@DisplayName("Standard Get test")
	class MehtodGet {

		@Test
		@DisplayName("Check No Content")
		void shouldNoContent() {
			RestAssuredMockMvc.responseSpecification = new ResponseSpecBuilder().build();
			// @formatter:off
	    	given().
			when().
		        get(RESOURCE).
			then().
				statusCode(HttpStatus.SC_NO_CONTENT);
			// @formatter:on
		}

	}

	@Nested
	@DisplayName("Standard Post test")
	class MehtodPost {

		@Test
		@DisplayName("Should Invalid Content Type")
		void shouldInvalidContentType() {
			// @formatter:off
	    	given().
	    		contentType(ContentType.TEXT).
	    		body("").
			when().
		        post(RESOURCE).
			then().
				statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE).
				body("message", is("Content type '"+ContentType.TEXT+";charset=ISO-8859-1' not supported")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", is(ContentType.TEXT+";charset=ISO-8859-1 media type is not supported. Supported media types are application/json application/*+json"));
			// @formatter:on
		}

		@Test
		@DisplayName("Should Invalid Body")
		void shouldNoBody() {
			// @formatter:off
	    	given().
    			body("").
			when().
		        post(RESOURCE).
			then().
				statusCode(HttpStatus.SC_BAD_REQUEST).
				body("message", is("Required request body")).
				body("details.size()", equalTo(1)).
				body("details[0]", is("body is missing"));
			// @formatter:on
		}
		
	}

	@Nested
	@DisplayName("Standard Put test")
	class MehtodPut {

		@Test
		@DisplayName("Should Method not supported")
		void shouldMethodNotSupported() {
			// @formatter:off
	    	given().
	    		body("").
			when().
		        put(RESOURCE).
			then().
				statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED).
				body("message", is("Request method 'PUT' not supported")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", startsWith("PUT method is not supported for this request. Supported methods are "));
			// @formatter:on
		}
		
	}

	@Nested
	@DisplayName("Standard Delete test")
	class MehtodDelete {

		@Test
		@DisplayName("Should Method not supported")
		void shouldMethodNotSupported() {
			// @formatter:off
	    	given().
	    		body("").
			when().
		        delete(RESOURCE).
			then().
				statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED).
				body("message", is("Request method 'DELETE' not supported")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", startsWith("DELETE method is not supported for this request. Supported methods are "));
			// @formatter:on
		}
		
	}
	
}
