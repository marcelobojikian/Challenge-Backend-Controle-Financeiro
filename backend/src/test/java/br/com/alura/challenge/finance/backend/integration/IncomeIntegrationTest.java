package br.com.alura.challenge.finance.backend.integration;

import static org.hamcrest.Matchers.endsWithIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.backend.controller.web.hateoas.IncomeReference;
import br.com.alura.challenge.finance.backend.integration.rest.BusinessExceptionTest;
import br.com.alura.challenge.finance.backend.integration.rest.FinanceListPostTest;
import br.com.alura.challenge.finance.backend.integration.standard.StandardResourceCrudFirstTest;
import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.parameter.FinanceConverter;
import br.com.alura.challenge.finance.backend.rest.response.ListHateoasFormat;
import br.com.alura.challenge.finance.backend.rest.response.NoContent;
import br.com.alura.challenge.finance.backend.rest.response.RecordNotFound;
import br.com.alura.challenge.finance.backend.rest.response.SingleHateoasFormat;
import br.com.alura.challenge.finance.backend.rest.standard.StandardDeleteTest;
import br.com.alura.challenge.finance.backend.rest.standard.StandardPutTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { ControleFinanceiroApplication.class })
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class IncomeIntegrationTest {
	
	static final String RESOURCE_NAME_COLLECTION_RELATION = IncomeReference.NAME_COLLECTION_RELATION;
	static final String RESOURCE = "/api/incomes";
	
	@LocalServerPort
    private int port;

	@Autowired
	ObjectMapper mapper;
	
	@BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

	@Nested
	@Order(1)
	@DisplayName("Standard All methods test")
	class StandardResourceCrudFirstTestImpl extends StandardResourceCrudFirstTest {
		
		public StandardResourceCrudFirstTestImpl() {
			super(RESOURCE);
		}

	}

	@Nested
    @Order(2)
	@DisplayName("Method Create")
	class MehtodPostimplements implements FinanceListPostTest<Income> {

		@Override
		public String getResource() {
			return RESOURCE;
		}

		@Override
		@DisplayName("Should create")
		@ParameterizedTest
		@CsvSource({
			"income::Salario    :2600  :01/08/2022",
			"income::Renda extra:400.30:15/08/2022",
			"income::Outros     :420   :01/08/2022",
			"income::TO UPDATE  :0     :01/01/2022"})
		public void each(@ConvertWith(FinanceConverter.class) Income entity) throws JsonProcessingException {
			String json = mapper.writeValueAsString(entity);
			System.out.println(json);
			run(json);
		}

		@Nested
		@DisplayName("Should entity already exist")
		class AlreadyExistPost implements BusinessExceptionTest {

			@Override
			public String getResource() {
				return RESOURCE;
			}
			
			@Override
			public String method() {
				return POST;
			}
			
			@Override
			public String body() {
				return "{\"descricao\":\"salario\",\"valor\":23,\"data\":\"03/08/2022\"}";
			}
			
		}

	}

	@Nested
    @Order(3)
	@DisplayName("Method GET ALL")
	class MethodGetAll implements ListHateoasFormat {

		@Override
		public String getResource() {
			return RESOURCE;
		}

		@Override
		public String collectionRelationName() {
			return RESOURCE_NAME_COLLECTION_RELATION;
		}
		
		@Override
		public void asserts(ValidatableResponse reponse) {
			ListHateoasFormat.super.asserts(reponse);
			// @formatter:off
			String body = reponse
                .body(listPath(), hasSize(greaterThan(0)))
                .body(itemProperty(0,"id"), is(1))
                .body(itemProperty(0,"descricao"), is("Salario"))
                .body(itemProperty(0,"valor"), equalTo(2600f))
                .body(itemProperty(0,"data"), is("01/08/2022"))
                .body(itemProperty(1,"id"), is(2))
                .body(itemProperty(1,"descricao"), is("Renda extra"))
                .body(itemProperty(1,"valor"), equalTo(400.30f))
                .body(itemProperty(1,"data"), is("15/08/2022"))
                .body(itemProperty(2,"id"), is(3))
                .body(itemProperty(2,"descricao"), is("Outros"))
                .body(itemProperty(2,"valor"), equalTo(420.0f))
                .body(itemProperty(2,"data"), is("01/08/2022"))
            .extract()
        		.asPrettyString();
			// @formatter:on
			System.out.println("all: "+body);
		}
	
	}

	@Nested
    @Order(4)
	@DisplayName("Method GET ID")
	class MethodGet implements RecordNotFound {

		@Override
		public String getResource() {
			return RESOURCE+"/-999";
		}
		
		@Nested
		@DisplayName("Should find id 1")
		class shouldFindId1 implements SingleHateoasFormat {

			@Override
			public String getResource() {
				return RESOURCE+"/1";
			}

			@Override
			public String collectionRelationName() {
				return RESOURCE_NAME_COLLECTION_RELATION;
			}
			
			@Override
			public void asserts(ValidatableResponse reponse) {
				SingleHateoasFormat.super.asserts(reponse);
				// @formatter:off
				String body = reponse
	                .body("id", is(1))
	                .body("descricao", is("Salario"))
	                .body("valor", equalTo(2600f))
	                .body("data", is("01/08/2022"))
                .extract()
            		.asPrettyString();
				// @formatter:on
				System.out.println("find id: "+body);
			}
			
		}

	}

	@Nested
    @Order(5)
	@DisplayName("Method PUT")
	class MehtodPut implements RecordNotFound {

		@Override
		public String getResource() {
			return RESOURCE+"/-999";
		}

		@Nested
		@DisplayName("Method update id 4")
		class OutrosPost implements StandardPutTest {

			@Override
			public String getResource() {
				return RESOURCE+"/4";
			}
			
			@Override
			public String body() {
				return "{\"id\":null,\"descricao\":\"UPDATED\",\"valor\":1,\"data\":\"03/07/2022\"}";
			}
			
			@Override
			public void asserts(ValidatableResponse reponse) {
				StandardPutTest.super.asserts(reponse);
				// @formatter:off
				String body = reponse
                    .body("id", is(4))
                    .body("descricao", is("UPDATED"))
                    .body("valor", equalTo(1))
                    .body("data", is("03/07/2022"))
                .extract()
            		.asPrettyString();
				// @formatter:on
				System.out.println("update: "+body);
			}
		}

		@Nested
		@DisplayName("Should not update because already exist the name in this mounth")
		class AlreadyExistPut implements BusinessExceptionTest {

			@Override
			public String getResource() {
				return RESOURCE+"/4";
			}
			
			@Override
			public String method() {
				return PUT;
			}
			
			@Override
			public String body() {
				return "{\"descricao\":\"salario\",\"valor\":1,\"data\":\"03/08/2022\"}";
			}
			
		}

	}

	@Nested
    @Order(6)
	@DisplayName("Method DELETE")
	class MethodDelete implements RecordNotFound {

		@Override
		public String getResource() {
			return RESOURCE+"/-999";
		}
		
		@Override
		public String method() {
			return "DELETE";
		}

		@Nested
		@DisplayName("Should delete id 4")
		class DeleteId implements StandardDeleteTest {

			@Override
			public String getResource() {
				return RESOURCE+"/4";
			}
			
		}

	}

	@Nested
    @Order(7)
	@DisplayName("Method GET by MONTH")
	class MethodGetMonth implements NoContent {

		@Override
		public String getResource() {
			return RESOURCE+"/2300/01";
		}

		@Nested
		@DisplayName("Should find AUGUST number")
		class MethodGetMonthAugustNumber implements ListHateoasFormat {

			@Override
			public String getResource() {
				return RESOURCE+"/2022/08";
			}

			@Override
			public String collectionRelationName() {
				return RESOURCE_NAME_COLLECTION_RELATION;
			}
			
			@Override
			public void asserts(ValidatableResponse reponse) {
				// @formatter:off
				String body = reponse
		                .statusCode(HttpStatus.SC_OK)
		                .contentType(ContentType.JSON)
		                .body(listPath(), hasSize(greaterThan(0)))
		            	.body(linkSelfHref(), endsWithIgnoringCase(RESOURCE+"/2022/AUGUST"))
	                    .body(listPath(), hasSize(3))
		            .extract()
		        		.asPrettyString();
				// @formatter:on
				System.out.println("month: "+body);
			}
		
		}

		@Nested
		@DisplayName("Should find AUGUST text")
		class MethodGetMonthAugustText implements ListHateoasFormat {

			@Override
			public String getResource() {
				return RESOURCE+"/2022/AUGUST";
			}

			@Override
			public String collectionRelationName() {
				return RESOURCE_NAME_COLLECTION_RELATION;
			}
			
			@Override
			public void asserts(ValidatableResponse reponse) {
				ListHateoasFormat.super.asserts(reponse);
				// @formatter:off
				String body = reponse
                    .body(listPath(), hasSize(3))
	            .extract()
	        		.asPrettyString();
				// @formatter:on
				System.out.println("month: "+body);
			}
		
		}

	}

}
