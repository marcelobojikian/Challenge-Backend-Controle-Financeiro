package br.com.alura.challenge.finance.test.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class AuthenticatedAccess {
	
    @Autowired
    private WebApplicationContext context;

	@LocalServerPort
	private int port;

	@Autowired
	public ObjectMapper mapper;

	@BeforeEach
	void setup() {
		
		RestAssured.port = this.port;

		// @formatter:off
		MockMvcRequestSpecification requestSpec = new MockMvcRequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.build();
		// @formatter:on
		RestAssuredMockMvc.requestSpecification = requestSpec;

		// @formatter:off
		ResponseSpecification responseSpec = new ResponseSpecBuilder()
				.expectContentType(ContentType.JSON)
				.build();
		// @formatter:on
		RestAssuredMockMvc.responseSpecification = responseSpec;
		
		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();		
        RestAssuredMockMvc.webAppContextSetup(context);

	}

    @AfterEach
    public void reset() {
        RestAssuredMockMvc.reset();
    }


}
