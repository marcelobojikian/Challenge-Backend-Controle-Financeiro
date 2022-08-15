package br.com.alura.challenge.finance.test.integration.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import br.com.alura.challenge.finance.test.tool.ResourceTest;
import br.com.alura.challenge.finance.test.tool.request.NoBody;
import br.com.alura.challenge.finance.test.tool.response.InvalidContentType;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

public class StandardCrudFirstPostTest {

	private final String method = ResourceTest.POST;
	private final String resource;
	private final ExtraValidationAllMethod[] extraValidationMethod;

	public StandardCrudFirstPostTest(String resource, ExtraValidationAllMethod... extraValidationMethod) {
		this.resource = resource;
		this.extraValidationMethod = extraValidationMethod;
	}

	@Nested
	@DisplayName("Check invalid content type")
	class InvalidContentTypeImpl implements InvalidContentType {

		@Override
		public String getResource() {
			return resource;
		}

		@Override
		public String method() {
			return method;
		}

		@Override
		public ContentType invlaidContentType() {
			return ContentType.TEXT;
		}

		@Override
		public void asserts(ValidatableResponse reponse) {
			InvalidContentType.super.asserts(reponse);
			for (ExtraValidationAllMethod validation : extraValidationMethod) {
				if (validation.method().equals(method)) {
					validation.assertInvalidContentType(reponse);
				}
			}
		}

	}

	@Nested
	@DisplayName("Check invalid body")
	class NoBodyImpl implements NoBody {

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
			NoBody.super.asserts(reponse);
			for (ExtraValidationAllMethod validation : extraValidationMethod) {
				if (validation.method().equals(method)) {
					validation.assertNoBody(reponse);
				}
			}
		}

	}

}
