package br.com.alura.challenge.finance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.controller.web.IncomeController;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = { IncomeController.class })
@AutoConfigureMockMvc
class IncomeControllerTest {

	static final String INCOME_API = "/api/incomes";

	@Autowired
	MockMvc mockMvc;

	@MockBean
	IncomeService incomeService;

	@MockBean
	ModelMapper modelMapper;

	static ObjectMapper mapper;

	@BeforeAll
	static void setup() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Nested
	@DisplayName("Method GET")
	class MethodGet {

		@Test
		@DisplayName("Should find the income with success")
		public void successFindIncome() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			Income savedEntity = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			given(incomeService.findById(any(Long.class))).willReturn(savedEntity);
			given(modelMapper.map(any(Income.class), eq(IncomeDTO.class))).willReturn(expectedDTO);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(INCOME_API+"/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("id").value(expectedDTO.getId()))
					.andExpect(jsonPath("descricao").value(expectedDTO.getDescricao()))
					.andExpect(jsonPath("valor").value(expectedDTO.getValor()))
					.andExpect(jsonPath("data").value(IncomeControllerTest.toString(expectedDTO.getData())));
			// @formatter:on

		}

		@Test
		@DisplayName("Should not find the income - status 404")
		public void errorFindIncome() throws Exception {

			given(incomeService.findById(any(Long.class))).willThrow(EntityNotFoundException.class);

			// @formatter:off
			MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(INCOME_API+"/-1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andDo(print())
					.andExpect(status().isNotFound());
			// @formatter:on

		}

	}

	@Nested
	@DisplayName("Method POST")
	class MehtodPost {

		@Test
		@DisplayName("Should create a income with success")
		public void successCreateIncome() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
			Income savedEntity = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			given(modelMapper.map(any(), eq(Income.class))).willReturn(savedEntity);
			given(modelMapper.map(any(), eq(IncomeDTO.class))).willReturn(expectedDTO);
			given(incomeService.save(any(Income.class))).willReturn(savedEntity);

			IncomeDTO entity = new IncomeDTO(null, "Teste", BigDecimal.ZERO, LocalDate.now());
			String json = mapper.writeValueAsString(entity);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .post(INCOME_API)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(json);
	        
	        mockMvc.perform(request)
					.andDo(print())
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
		@DisplayName("Should update the income with success")
		public void successUpdateIncome() throws Exception {

			IncomeDTO expectedDTO = new IncomeDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
			Income entityExpected = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

			given(modelMapper.map(any(), eq(Income.class))).willReturn(entityExpected);
			given(incomeService.update(any(Long.class), any(Income.class))).willReturn(entityExpected);
			given(modelMapper.map(any(), eq(IncomeDTO.class))).willReturn(expectedDTO);

			IncomeDTO entity = new IncomeDTO(1l, "Teste 2", BigDecimal.ONE, toDate("10/09/2022"));
			String json = mapper.writeValueAsString(entity);

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .put(INCOME_API+"/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .content(json);
	        
	        mockMvc.perform(request)
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("id").value(expectedDTO.getId()))
					.andExpect(jsonPath("descricao").value(expectedDTO.getDescricao()))
					.andExpect(jsonPath("valor").value(expectedDTO.getValor()))
					.andExpect(jsonPath("data").value(IncomeControllerTest.toString(expectedDTO.getData())));
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
