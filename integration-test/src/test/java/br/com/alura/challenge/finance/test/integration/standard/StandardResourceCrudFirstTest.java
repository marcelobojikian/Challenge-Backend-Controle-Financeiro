package br.com.alura.challenge.finance.test.integration.standard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardResourceCrudFirstTest {

	Logger log = LoggerFactory.getLogger(StandardResourceCrudFirstTest.class);

	private final String resource;
	private final ExtraValidationAllMethod[] extraValidationMethod;

	public StandardResourceCrudFirstTest(String resource, ExtraValidationAllMethod... extraValidationMethod) {
		this.resource = resource;
		this.extraValidationMethod = extraValidationMethod;
		log.info("Validacao de CRUD para resource {}", resource);
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
