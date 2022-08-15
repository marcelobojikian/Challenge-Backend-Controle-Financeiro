package br.com.alura.challenge.finance.test.tool;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public interface ResourceTest {

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";

	String getResource();
	
	RequestSpecification scene();
	
	default void run() {
		
		RequestSpecification scene =scene().when();

		ValidatableResponse reponse = method(scene).then();

    	asserts(reponse); 
		
	}
	
	void asserts(ValidatableResponse reponse);
	
	default String method() {
		return GET;
	}

	default Response method(RequestSpecification when) {
		switch (method()) {
		case POST:
			return when.post(getResource());
		case PUT:
			return when.put(getResource());
		case DELETE:
			return when.delete(getResource());
		default:
			return when.get(getResource());
		}
	};

}
