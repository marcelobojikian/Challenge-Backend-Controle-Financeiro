package br.com.alura.challenge.finance.test.integration.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import br.com.alura.challenge.finance.test.tool.request.MethodNotSupported;
import io.restassured.response.ValidatableResponse;

public class StandardCrudFirstPutTest {

	private final String method = ResourceTest.PUT;
	private final String resource;
	private final ExtraValidationAllMethod[] extraValidationMethod;

	public StandardCrudFirstPutTest(String resource, ExtraValidationAllMethod... extraValidationMethod) {
		this.resource = resource;
		this.extraValidationMethod = extraValidationMethod;
	}

	@Nested
	@DisplayName("Check method not supported")
	class MethodNotSupportedImpl implements MethodNotSupported {

		@Override
		public String getResource() {
			return resource;
		}

		@Override
		public String method() {
			return method;
		}

		@Override
		public void asserts(ValidatableResponse reponse) {
			MethodNotSupported.super.asserts(reponse);
			for (ExtraValidationAllMethod validation : extraValidationMethod) {
				if (validation.method().equals(method)) {
					validation.assertInvalidContentType(reponse);
				}
			}
		}

	}

}
