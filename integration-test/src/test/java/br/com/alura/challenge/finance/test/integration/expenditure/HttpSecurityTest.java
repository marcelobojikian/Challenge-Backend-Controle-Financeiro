package br.com.alura.challenge.finance.test.integration.expenditure;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.test.integration.GuestAccess;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { ControleFinanceiroApplication.class })
@DisplayName("Expenditure Http Security Test")
public class HttpSecurityTest extends GuestAccess {
	
	final static int UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
	static final String RESOURCE = "/api/expenditures";

	@Test
	public void methodGetAll() {
		// @formatter:off
    	given().
		when().
	        get(RESOURCE).
		then().
	        statusCode(UNAUTHORIZED);
		// @formatter:on
	}

	@Test
	public void methodGetId() {
		// @formatter:off
    	given().
		when().
	        get(RESOURCE+"/1").
		then().
	        statusCode(UNAUTHORIZED);
		// @formatter:on
	}

	@Test
	public void methodPost() {
		// @formatter:off
    	given().
		when().
	        post(RESOURCE).
		then().
	        statusCode(UNAUTHORIZED);
		// @formatter:on
	}

	@Test
	public void methodPut() {
		// @formatter:off
    	given().
		when().
	        put(RESOURCE+"/1").
		then().
	        statusCode(UNAUTHORIZED);
		// @formatter:on
	}

	@Test
	public void methodDelete() {
		// @formatter:off
    	given().
		when().
	        delete(RESOURCE+"/1").
		then().
	        statusCode(UNAUTHORIZED);
		// @formatter:on
	}

}
