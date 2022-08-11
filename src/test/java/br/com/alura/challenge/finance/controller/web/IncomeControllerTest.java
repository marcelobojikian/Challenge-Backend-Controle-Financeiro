package br.com.alura.challenge.finance.controller.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.config.ApplicationConfig;
import br.com.alura.challenge.finance.config.DateFormatConfig;
import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.controller.dto.mapper.IncomeMapperConverter;
import br.com.alura.challenge.finance.controller.web.hateoas.IncomeReference;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = { IncomeController.class, IncomeReference.class })
@AutoConfigureMockMvc
@Import({ DateFormatConfig.class, ApplicationConfig.class })
class IncomeControllerTest {

	static final String URL_API = "/api/incomes";

	@Autowired
	ObjectMapper mapper;

	@Autowired
	IncomeReference ref;

	@Autowired
	IncomeMapperConverter converter;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IncomeService service;

	@Nested
	@DisplayName("Method GET by MONTH")
	class MethodGetMonth {

		@Test
		@DisplayName("Should find AUGUST")
		public void successFind() throws Exception {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList(expected));
//			given(modelMapper.map(any(Object.class), any(Type.class))).willReturn(Arrays.asList(expectedDTO));

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/2022/"+Month.AUGUST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$._embedded.incomes", hasSize(1)))
					.andExpect(jsonPath("$..id", hasItems(1)))
					.andExpect(jsonPath("$..descricao", hasItems("Test")))
					.andExpect(jsonPath("$..valor", hasItems(23)))
					.andExpect(jsonPath("$..data", hasItems("03/08/2022")));
			// @formatter:on

		}

		@Test
		@DisplayName("Should no content")
		public void shouldNoContent() throws Exception {

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList());
//			given(modelMapper.map(any(Object.class), any(Type.class))).willReturn(Arrays.asList());

			// @formatter:off
			MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/2022/"+Month.AUGUST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);

	        mockMvc.perform(request)
					.andExpect(status().isNoContent());
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method GET ALL")
	class MethodGetAll {

		@Test
		@DisplayName("Should find all")
		public void successFind() throws Exception {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income secondExpected = new Income(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"));

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			IncomeDTO secondExpectedDTO = new IncomeDTO(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"));

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList(expected, secondExpected));
//			given(modelMapper.map(any(Object.class), any(Type.class)))
//					.willReturn(Arrays.asList(expectedDTO, secondExpectedDTO));

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$..id", hasItems(1,2)))
					.andExpect(jsonPath("$..descricao", hasItems("Test", "Test 2")))
					.andExpect(jsonPath("$..valor", hasItems(23, 44)))
					.andExpect(jsonPath("$..data", hasItems("03/08/2022","11/07/2022")));
			// @formatter:on

		}

		@Test
		@DisplayName("Should no content")
		public void shouldNoContent() throws Exception {

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList());
//			given(modelMapper.map(any(Object.class), any(Type.class))).willReturn(Arrays.asList());

			// @formatter:off
			MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);

	        mockMvc.perform(request)
					.andExpect(status().isNoContent());
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method GET ID")
	class MethodGet {

		@Test
		@DisplayName("Should find")
		public void successFind() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			Income savedEntity = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			given(service.findById(any(Long.class))).willReturn(savedEntity);
//			given(modelMapper.map(any(Income.class), eq(IncomeDTO.class))).willReturn(expectedDTO);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("id").value(expectedDTO.getId()))
					.andExpect(jsonPath("descricao").value(expectedDTO.getDescricao()))
					.andExpect(jsonPath("valor").value(expectedDTO.getValor()))
					.andExpect(jsonPath("data").value(IncomeControllerTest.toString(expectedDTO.getData())));
			// @formatter:on

		}

		@Test
		@DisplayName("Should not find - status 404")
		public void shouldNotFind() throws Exception {

			EntityNotFoundException expected = new EntityNotFoundException("Entity not found");

			given(service.findById(any(Long.class))).willThrow(expected);

			// @formatter:off
			MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/-1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.message", is("Record not found")))
					.andExpect(jsonPath("$.details", hasSize(1)))
					.andExpect(jsonPath("$.details.[0]", is("Entity not found")));;
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method POST")
	class MehtodPost {

		@Test
		@DisplayName("Should create")
		public void successCreate() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			Income savedEntity = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

//			given(modelMapper.map(any(), eq(Income.class))).willReturn(savedEntity);
//			given(modelMapper.map(any(), eq(IncomeDTO.class))).willReturn(expectedDTO);
			given(service.save(any(Income.class))).willReturn(savedEntity);

			IncomeDTO entity = new IncomeDTO(null, "Teste", BigDecimal.ZERO, LocalDate.now());
			String json = mapper.writeValueAsString(entity);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .post(URL_API)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(json);
	        
	        mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(jsonPath("id").value(expectedDTO.getId()))
					.andExpect(jsonPath("descricao").value(expectedDTO.getDescricao()))
					.andExpect(jsonPath("valor").value(expectedDTO.getValor()))
					.andExpect(jsonPath("data").value(IncomeControllerTest.toString(expectedDTO.getData())));
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method PUT")
	class MehtodPut {

		@Test
		@DisplayName("Should update")
		public void successUpdate() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
			Income entityExpected = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			given(service.findById(any(Long.class))).willReturn(entityExpected);
			given(service.save(any(Income.class))).willReturn(entityExpected);
//			given(modelMapper.map(any(), eq(IncomeDTO.class))).willReturn(expectedDTO);

			IncomeDTO entity = new IncomeDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
			String json = mapper.writeValueAsString(entity);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .put(URL_API+"/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(json);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("id").value(expectedDTO.getId()))
					.andExpect(jsonPath("descricao").value(expectedDTO.getDescricao()))
					.andExpect(jsonPath("valor").value(expectedDTO.getValor()))
					.andExpect(jsonPath("data").value(IncomeControllerTest.toString(expectedDTO.getData())));
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method DELETE")
	class MethodDelete {

		@Test
		@DisplayName("Should delete")
		public void successDelete() throws Exception {

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .delete(URL_API+"/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isNoContent());
			// @formatter:on

		}

		@Test
		@DisplayName("Should not delete - status 404")
		public void shouldNotDelete() throws Exception {

			EntityNotFoundException expected = new EntityNotFoundException("Entity not found");

			given(service.findById(any(Long.class))).willThrow(expected);

			// @formatter:off
			MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/-1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
			
	        mockMvc.perform(request)
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.message", is("Record not found")))
					.andExpect(jsonPath("$.details", hasSize(1)))
					.andExpect(jsonPath("$.details.[0]", is("Entity not found")));;
			// @formatter:on

		}

	}

	public static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public static String toString(LocalDate localDate) {
		return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
