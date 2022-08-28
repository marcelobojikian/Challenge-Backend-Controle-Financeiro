package br.com.alura.challenge.finance.test.integration.expenditure;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWithIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.rest.controller.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.test.integration.AuthenticatedAccess;
import br.com.alura.challenge.finance.test.integration.ResponseLinkHateoas;
import br.com.alura.challenge.finance.test.integration.converter.FinanceConverter;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WithMockUser(username = "user")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { ControleFinanceiroApplication.class })
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Expenditure First Scene Test")
public class FirstSceneTest extends AuthenticatedAccess implements ResponseLinkHateoas {

	static final String RESOURCE = "/api/expenditures";

	@Override
	public String collectionRelationName() {
		return ExpenditureReference.NAME_COLLECTION_RELATION;
	}

	@Nested
	@Order(1)
	@DisplayName("Method Create")
	class MehtodPost {

		@DisplayName("Should create finance")
		@ParameterizedTest
		// @formatter:off
		@CsvSource({ "expenditure::Salario    :2600  :01/08/2022",
					 "expenditure::Renda extra:400.30:15/08/2022",
					 "expenditure::Outros     :420   :01/08/2022",
					 "expenditure::TO UPDATE  :0     :01/01/2022" })
		// @formatter:on
		void shouldCeateFinanceList(@ConvertWith(FinanceConverter.class) Expenditure entity) throws JsonProcessingException {
			String json = mapper.writeValueAsString(entity);

			// @formatter:off
	    	given().
		        body(json).
			when().
		        post(RESOURCE).
			then().
				statusCode(HttpStatus.SC_CREATED).
	            body(linkSelfHref(), not(empty())).
	            body(linkListHref(), not(empty()));
			// @formatter:on
		}

	}

	@Nested
	@Order(2)
	@DisplayName("Method Create already exist")
	class MehtodPostAlreadyExist {

		@Test
		@DisplayName("Should no create entity")
		void shouldNoCreateFinance() {
			// @formatter:off
	    	given().
		        body("{\"descricao\":\"salario\",\"valor\":23,\"data\":\"03/08/2022\"}").
			when().
		        post(RESOURCE).
			then().
				statusCode(HttpStatus.SC_BAD_REQUEST).
	            body("message", is("Error business")).
	            body("details.size()", equalTo(1)).
	            body("details[0]", is("There is this finance for this month"));
			// @formatter:on
		}

	}

	@Nested
	@Order(3)
	@DisplayName("Method GET ALL")
	class MethodGetAll {

		@Test
		@DisplayName("Should list with Hateoas format")
		void shouldListFinanceWithHateoasFormat() {
			// @formatter:off
			given().
			when().
		        get(RESOURCE).
			then().
	            statusCode(HttpStatus.SC_OK).
	            body(listPath(), hasSize(greaterThan(0))).
	            body(linkSelfHref(), endsWithIgnoringCase(RESOURCE)).
                body(itemProperty(0,"id"), is(1)).
                body(itemProperty(0,"descricao"), is("Salario")).
                body(itemProperty(0,"valor"), equalTo(2600f)).
                body(itemProperty(0,"data"), is("01/08/2022")).
                body(itemProperty(1,"id"), is(2)).
                body(itemProperty(1,"descricao"), is("Renda extra")).
                body(itemProperty(1,"valor"), equalTo(400.30f)).
                body(itemProperty(1,"data"), is("15/08/2022")).
                body(itemProperty(2,"id"), is(3)).
                body(itemProperty(2,"descricao"), is("Outros")).
                body(itemProperty(2,"valor"), equalTo(420.0f)).
                body(itemProperty(2,"data"), is("01/08/2022"));
			// @formatter:on
		}

	}

	@Nested
	@Order(4)
	@DisplayName("Method GET ID")
	class MethodGet {

		@Test
		@DisplayName("Should Record not found")
		void shouldFinanceNotFound() {
			// @formatter:off
	    	given().
			when().
		        get(RESOURCE + "/-999").
			then().
	        	statusCode(HttpStatus.SC_NOT_FOUND).
		        body("message", is("Record not found")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", is("Entity not found"));
			// @formatter:on
		}

		@Test
		@DisplayName("Should find id 1 with Hateoas format")
		void shouldFindId1WithHateoasFormat() {
			// @formatter:off
	    	given().
			when().
		        get(RESOURCE + "/1").
			then().
	            statusCode(HttpStatus.SC_OK).
	            body(linkSelfHref(), not(empty())).
	            body(linkListHref(), not(empty())).
	            body("id", is(1)).
	            body("descricao", is("Salario")).
	            body("valor", equalTo(2600f)).
	            body("data", is("01/08/2022"));
			// @formatter:on
		}

	}

	@Nested
	@Order(5)
	@DisplayName("Method PUT")
	class MehtodPut {

		@Test
		@DisplayName("Should Record not found")
		void shouldFinanceNotFound() {
			// @formatter:off
	    	given().
    			body("{\"id\":null,\"descricao\":\"UPDATED\",\"valor\":1,\"data\":\"03/07/2022\"}").
			when().
		        put(RESOURCE + "/-999").
			then().
	        	statusCode(HttpStatus.SC_NOT_FOUND).
		        body("message", is("Record not found")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", is("Entity not found"));
			// @formatter:on
		}

		@Test
		@DisplayName("Should update id 4")
		void shouldUpdateId4() {
			// @formatter:off
	    	given().
	    		body("{\"id\":null,\"descricao\":\"UPDATED\",\"valor\":1,\"data\":\"03/07/2022\"}").
			when().
		        put(RESOURCE + "/4").
			then().
	        	statusCode(HttpStatus.SC_OK).
                body("id", is(4)).
                body("descricao", is("UPDATED")).
                body("valor", equalTo(1)).
                body("data", is("03/07/2022"));
			// @formatter:on
		}

		@Test
		@DisplayName("Should not update because already exist the name in this mounth")
		void shouldNotUpdateBecauseAlreadyExistTheNameInThisMounth() {
			// @formatter:off
	    	given().
	    		body("{\"descricao\":\"salario\",\"valor\":1,\"data\":\"03/08/2022\"}").
			when().
		        put(RESOURCE + "/4").
			then().
                statusCode(HttpStatus.SC_BAD_REQUEST).
                body("message", is("Error business")).
                body("details.size()", equalTo(1)).
                body("details[0]", is("There is this finance for this month"));
			// @formatter:on
		}
		
	}

	@Nested
	@Order(6)
	@DisplayName("Method DELETE")
	class MethodDelete {

		@Test
		@DisplayName("Should Record not found")
		void shouldFinanceNotFound() {
			// @formatter:off
	    	given().
			when().
		        delete(RESOURCE + "/-999").
			then().
	        	statusCode(HttpStatus.SC_NOT_FOUND).
		        body("message", is("Record not found")).
		        body("details.size()", equalTo(1)).
		        body("details[0]", is("Entity not found"));
			// @formatter:on
		}

		@Test
		@DisplayName("Should delete id 4")
		void shouldDeleteId4() {

			RestAssuredMockMvc.responseSpecification = new ResponseSpecBuilder().build();

			// @formatter:off
	    	given().
			when().
		        delete(RESOURCE + "/4").
			then().
	        	statusCode(HttpStatus.SC_OK);
			// @formatter:on
		}

	}

	@Nested
	@Order(7)
	@DisplayName("Method GET by MONTH")
	class MethodGetMonth {

		@Test
		@DisplayName("Should Record not found")
		void shouldFinanceNotFound() {

			RestAssuredMockMvc.responseSpecification = new ResponseSpecBuilder().build();

			// @formatter:off
	    	given().
			when().
		        get(RESOURCE + "/2300/01").
			then().
	        	statusCode(HttpStatus.SC_NO_CONTENT);
			// @formatter:on
		}

		@Test
		@DisplayName("Should find AUGUST number")
		void soudlFindAugustNumber() {
			// @formatter:off
	    	given().
			when().
		        get(RESOURCE + "/2022/08").
			then().
	            statusCode(HttpStatus.SC_OK).
	            body(listPath(), hasSize(greaterThan(0))).
	            body(linkSelfHref(), endsWithIgnoringCase(RESOURCE+"/2022/AUGUST")).
                body(listPath(), hasSize(3));
			// @formatter:on
		}

		@Test
		@DisplayName("Should find AUGUST text")
		void soudlFindAugustText() {
			// @formatter:off
	    	given().
			when().
		        get(RESOURCE + "/2022/AUGUST").
			then().
	            statusCode(HttpStatus.SC_OK).
	            body(listPath(), hasSize(greaterThan(0))).
	            body(linkSelfHref(), endsWithIgnoringCase(RESOURCE + "/2022/AUGUST")).
                body(listPath(), hasSize(3));
			// @formatter:on
		}

	}

}
