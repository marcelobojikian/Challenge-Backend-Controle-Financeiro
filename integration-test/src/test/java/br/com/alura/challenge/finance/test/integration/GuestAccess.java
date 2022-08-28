package br.com.alura.challenge.finance.test.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GuestAccess {

	@LocalServerPort
	private int port;

	@Autowired
	ObjectMapper mapper;

	@BeforeEach
	void setup() {
		
		RestAssured.port = this.port;

		// @formatter:off
		RequestSpecification requestSpec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.build();
		// @formatter:on
		RestAssured.requestSpecification = requestSpec;

		// @formatter:off
		ResponseSpecification responseSpec = new ResponseSpecBuilder()
				.build();
		// @formatter:on
		RestAssured.responseSpecification = responseSpec;
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
	}

}
