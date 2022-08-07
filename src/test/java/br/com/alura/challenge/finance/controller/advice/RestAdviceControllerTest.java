package br.com.alura.challenge.finance.controller.advice;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.alura.challenge.finance.config.DateFormatConfig;
import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SampleController.class)
@ContextConfiguration(classes = { SampleController.class, RestAdviceController.class })
@AutoConfigureMockMvc
@Import(DateFormatConfig.class)
class RestAdviceControllerTest {

	static final String URL_API = "/api/exception";
	static final String URL_API_BEAN_INVALID = URL_API + SampleController.PATH_BEAN_INVALID;
	static final String URL_API_NOT_FOUND = URL_API + SampleController.PATH_BEAN_NOT_FOUND;
	static final String URL_API_BUSINESS_EXCEPTION = URL_API + SampleController.PATH_BUSINESS_EXCEPTION;
	static final String URL_API_EXCEPTION = URL_API + SampleController.PATH_EXCEPTION;

	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("Should throw internal server error")
	public void shouldThrowInternalServerError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(URL_API_EXCEPTION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(request)
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.message", is("Server Error")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("Generic Error")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw not empty body error")
	public void shouldThrowNotEmptyBodyError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_API_BEAN_INVALID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Required request body")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("body is missing")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw invalid field error")
	public void shouldThrowInvalidFieldError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_API_BEAN_INVALID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"value\": \"123\", \"description\": \"aaa\" }");
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid field")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("date: must not be null")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw invalid value field error")
	public void shouldThrowInvalidValueFieldError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_API_BEAN_INVALID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"value\": \"as\", \"date\": \"12/12/2022\" }"); 
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid field")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("Value 'as' is not valid")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw invalid date field error")
	public void shouldThrowInvalidDateFieldError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_API_BEAN_INVALID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"value\": \"123\", \"date\": \"231\" }"); 
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid field")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("Text '231' could not be parsed at index 2")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw invalid fields error")
	public void shouldThrowInvalidFieldsError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(URL_API_BEAN_INVALID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"value\": \"\" }");
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Invalid fields")))
				.andExpect(jsonPath("$.details", hasSize(3)))
				.andExpect(jsonPath("$.details", hasItems("value: must not be null","date: must not be null", "description: must not be empty")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw not found error")
	public void shouldThrowNotFoundError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(URL_API_NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(request)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message", is("Record not found")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("Entity not found")));
		// @formatter:on

	}

	@Test
	@DisplayName("Should throw business error")
	public void shouldThrowBusinessError() throws Exception {

		// @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(URL_API_BUSINESS_EXCEPTION)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Error business")))
				.andExpect(jsonPath("$.details", hasSize(1)))
				.andExpect(jsonPath("$.details.[0]", is("Error business")));
		// @formatter:on

	}

}

@RestController
@RequestMapping(RestAdviceControllerTest.URL_API)
class SampleController {

	static final String PATH_BEAN_INVALID = "/bean/validator";
	static final String PATH_BEAN_NOT_FOUND = "/not_found";
	static final String PATH_BUSINESS_EXCEPTION = "/business_exception";
	static final String PATH_EXCEPTION = "/exception";

	@PostMapping(PATH_BEAN_INVALID)
	public void throwMethodArgumentNotValidException(@RequestBody @Valid SimpleBean propertie) {
	}

	@GetMapping(PATH_BEAN_NOT_FOUND)
	public void throwEntityNotFoundException() {
		throw new EntityNotFoundException("Entity not found");
	}

	@GetMapping(PATH_BUSINESS_EXCEPTION)
	public void throwBusinessException() {
		throw new BusinessException("Error business");
	}

	@GetMapping(PATH_EXCEPTION)
	public void throwException() throws Exception {
		throw new Exception("Generic Error");
	}

}

class SimpleBean {

	@NotEmpty
	String description;

	@NotNull
	BigDecimal value;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	LocalDate date;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
