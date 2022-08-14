package br.com.alura.challenge.finance.integration.standard;

import io.restassured.response.ValidatableResponse;

public interface ExtraValidationAllMethod {
	
	String method();
	
	void assertInvalidContentType(ValidatableResponse reponse);
	
	void assertNoBody(ValidatableResponse reponse);
	
	void assertNoContent(ValidatableResponse reponse);
	
	void assertMethodNotSupported(ValidatableResponse reponse);

}
