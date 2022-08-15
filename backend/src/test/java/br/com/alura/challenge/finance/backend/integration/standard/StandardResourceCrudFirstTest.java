package br.com.alura.challenge.finance.backend.integration.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

public class StandardResourceCrudFirstTest {
	
	private final String resource;
	private final ExtraValidationAllMethod[] extraValidationMethod;
	
	public StandardResourceCrudFirstTest(String resource, ExtraValidationAllMethod... extraValidationMethod) {
		this.resource = resource;
		this.extraValidationMethod = extraValidationMethod;
	}

	@Nested
	@DisplayName("Standard Get test")
	class StandardGetTestImpl extends StandardCrudFirstGetTest {
		
		public StandardGetTestImpl() {
			super(resource, extraValidationMethod);
		}

	}

	@Nested
	@DisplayName("Standard Post test")
	class StandardPostTestImpl extends StandardCrudFirstPostTest {
		
		public StandardPostTestImpl() {
			super(resource, extraValidationMethod);
		}
	}

	@Nested
	@DisplayName("Standard Put test")
	class StandardPutTestImpl extends StandardCrudFirstPutTest {
		
		public StandardPutTestImpl() {
			super(resource, extraValidationMethod);
		}
	}

	@Nested
	@DisplayName("Standard Delete test")
	class StandardDeleteTestImpl extends StandardCrudFirstDeleteTest {
		
		public StandardDeleteTestImpl() {
			super(resource, extraValidationMethod);
		}
	}

}
