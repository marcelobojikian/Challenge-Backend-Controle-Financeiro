package br.com.alura.challenge.finance.controller.error;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SampleController.class)
@ContextConfiguration(classes = { SampleController.class, ControllerAdvice.class })
@AutoConfigureMockMvc
class ControllerAdviceTest {

	static final String URL_API = "/api/exception";
	static final String URL_API_BEAN_INVALID = URL_API + SampleController.PATH_BEAN_INVALID;
	static final String URL_API_NOT_FOUND = URL_API + SampleController.PATH_BEAN_NOT_FOUND;
	static final String URL_API_BUSINESS_EXCEPTION = URL_API + SampleController.PATH_BUSINESS_EXCEPTION;

	@Autowired
	MockMvc mockMvc;

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
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message", is("Required request body")))
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors.[0]", is("body is missing")));
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
                .content("{\"value\": \"123\" }");
        
        mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message", is("Invalid field")))
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors.[0]", is("description: must not be empty")));
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
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message", is("Invalid fields")))
				.andExpect(jsonPath("$.errors", hasSize(2)))
				.andExpect(jsonPath("$.errors", hasItems("value: must not be empty","description: must not be empty")));
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
				.andExpect(jsonPath("$.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.message", is("Entity not found")))
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors.[0]", is("Entity not found")));
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
				.andExpect(jsonPath("$.status", is("BAD_REQUEST")))
				.andExpect(jsonPath("$.message", is("Error business")))
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors.[0]", is("Error business")));
		// @formatter:on

	}

}

@RestController
@RequestMapping(ControllerAdviceTest.URL_API)
class SampleController {

	static final String PATH_BEAN_INVALID = "/bean/validator";
	static final String PATH_BEAN_NOT_FOUND = "/not_found";
	static final String PATH_BUSINESS_EXCEPTION = "/business_exception";

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

}

class SimpleBean {

	@NotEmpty
	String value;

	@NotEmpty
	String description;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
