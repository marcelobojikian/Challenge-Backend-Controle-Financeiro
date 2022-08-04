package br.com.alura.challenge.finance.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

	@Test
	@DisplayName("Should create a income with success")
	public void successCreateIncome() throws Exception {

		// cenario
		IncomeDTO entityDTO = new IncomeDTO(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));
		Income savedEntity = new Income(1l, "Teste", BigDecimal.ZERO, toDate("03/09/2022"));

		// execucao
		BDDMockito.given(modelMapper.map(Mockito.any(), Mockito.eq(Income.class))).willReturn(savedEntity);
		BDDMockito.given(modelMapper.map(Mockito.any(), Mockito.eq(IncomeDTO.class))).willReturn(entityDTO);
		BDDMockito.given(incomeService.create(Mockito.any(Income.class))).willReturn(savedEntity);

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
				.andExpect(jsonPath("id").value(entityDTO.getId()))
				.andExpect(jsonPath("descricao").value(entityDTO.getDescricao()))
				.andExpect(jsonPath("valor").value(entityDTO.getValor()))
				.andExpect(jsonPath("data").value(toString(entityDTO.getData())));
		// @formatter:on

	}

	LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	String toString(LocalDate localDate) {
		return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
