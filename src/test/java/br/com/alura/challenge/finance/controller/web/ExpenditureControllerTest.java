package br.com.alura.challenge.finance.controller.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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

import br.com.alura.challenge.finance.config.DateFormatConfig;
import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.service.ExpenditureService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = { ExpenditureController.class })
@AutoConfigureMockMvc
@Import(DateFormatConfig.class)
class ExpenditureControllerTest {

	static final String URL_API = "/api/expenditures";

	@Autowired
	ObjectMapper mapper;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ExpenditureService service;

	@MockBean
	ModelMapper modelMapper;

	@Nested
	@DisplayName("Method GET ALL")
	class MethodGetAll {

		@Test
		@DisplayName("Should find all")
		public void successFind() throws Exception {

			Expenditure expected = new Expenditure(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Expenditure secondExpected = new Expenditure(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"));

			ExpenditureDTO expectedDTO = new ExpenditureDTO(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			ExpenditureDTO secondExpectedDTO = new ExpenditureDTO(2l, "Test 2", BigDecimal.valueOf(44),
					toDate("11/07/2022"));

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList(expected, secondExpected));
			given(modelMapper.map(any(Iterable.class), any(Type.class)))
					.willReturn(Arrays.asList(expectedDTO, secondExpectedDTO));

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"?descricao=aaa")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$..id", hasItems(1,2)))
					.andExpect(jsonPath("$..descricao", hasItems("Test", "Test 2")))
					.andExpect(jsonPath("$..valor", hasItems(23, 44)))
					.andExpect(jsonPath("$..data", hasItems("03/08/2022","11/07/2022")))
					.andExpect(jsonPath("$..categoria", hasItems("OUTRAS","OUTRAS")));
			// @formatter:on

		}

		@Test
		@DisplayName("Should no content")
		public void shouldNoContent() throws Exception {

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList());
			given(modelMapper.map(any(Object.class), any(Type.class))).willReturn(Arrays.asList());

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

			ExpenditureDTO expectedDTO = new ExpenditureDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			Expenditure savedEntity = new Expenditure(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			given(service.findById(any(Long.class))).willReturn(savedEntity);
			given(modelMapper.map(any(Expenditure.class), eq(ExpenditureDTO.class))).willReturn(expectedDTO);

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
					.andExpect(jsonPath("data").value(ExpenditureControllerTest.toString(expectedDTO.getData())))
					.andExpect(jsonPath("categoria").value("OUTRAS"));
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

			ExpenditureDTO expectedDTO = new ExpenditureDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			Expenditure savedEntity = new Expenditure(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			given(modelMapper.map(any(), eq(Expenditure.class))).willReturn(savedEntity);
			given(modelMapper.map(any(), eq(ExpenditureDTO.class))).willReturn(expectedDTO);
			given(service.save(any(Expenditure.class))).willReturn(savedEntity);

			ExpenditureDTO entity = new ExpenditureDTO(null, "Teste", BigDecimal.ZERO, LocalDate.now());
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
					.andExpect(jsonPath("data").value(ExpenditureControllerTest.toString(expectedDTO.getData())))
					.andExpect(jsonPath("categoria").value("OUTRAS"));
			// @formatter:on

		}

		@Test
		@DisplayName("Should create with category")
		public void successCreateWithCategory() throws Exception {

			ExpenditureDTO expectedDTO = new ExpenditureDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"),
					Categoria.IMPREVISTOS);
			Expenditure savedEntity = new Expenditure(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"),
					Categoria.IMPREVISTOS);

			given(modelMapper.map(any(), eq(Expenditure.class))).willReturn(savedEntity);
			given(modelMapper.map(any(), eq(ExpenditureDTO.class))).willReturn(expectedDTO);
			given(service.save(any(Expenditure.class))).willReturn(savedEntity);

			ExpenditureDTO entity = new ExpenditureDTO(null, "Teste", BigDecimal.ZERO, LocalDate.now());
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
					.andExpect(jsonPath("data").value(ExpenditureControllerTest.toString(expectedDTO.getData())))
					.andExpect(jsonPath("categoria").value("IMPREVISTOS"));
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method PUT")
	class MehtodPut {

		@Test
		@DisplayName("Should update")
		public void successUpdate() throws Exception {

			ExpenditureDTO expectedDTO = new ExpenditureDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
			Expenditure entityExpected = new Expenditure(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			given(service.findById(any(Long.class))).willReturn(entityExpected);
			given(service.save(any(Expenditure.class))).willReturn(entityExpected);
			given(modelMapper.map(any(), eq(ExpenditureDTO.class))).willReturn(expectedDTO);

			ExpenditureDTO entity = new ExpenditureDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
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
					.andExpect(jsonPath("data").value(ExpenditureControllerTest.toString(expectedDTO.getData())))
					.andExpect(jsonPath("categoria").value("OUTRAS"));
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
