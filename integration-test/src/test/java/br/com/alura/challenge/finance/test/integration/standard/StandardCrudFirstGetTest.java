package br.com.alura.challenge.finance.test.integration.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import br.com.alura.challenge.finance.test.tool.response.NoContent;
import io.restassured.response.ValidatableResponse;

public class StandardCrudFirstGetTest {

	private final String method = ResourceTest.GET;
	private final String resource;
	private final ExtraValidationAllMethod[] extraValidationMethod;

	public StandardCrudFirstGetTest(String resource, ExtraValidationAllMethod... extraValidationMethod) {
		this.resource = resource;
		this.extraValidationMethod = extraValidationMethod;
	}

	@Nested
	@DisplayName("Check no content")
	class NoContentImpl implements NoContent {

		Logger log = LoggerFactory.getLogger(NoContentImpl.class);

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
			NoContent.super.asserts(reponse);
			for (ExtraValidationAllMethod validation : extraValidationMethod) {
				if (validation.method().equals(method)) {
					log.debug("Extra validation mathod [{}] in {}", method, resource);
					validation.assertNoContent(reponse);
				}
			}

		}

	}

}
