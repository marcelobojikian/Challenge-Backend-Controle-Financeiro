package br.com.alura.challenge.finance.controller.web;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
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

import br.com.alura.challenge.finance.config.DateFormatConfig;
import br.com.alura.challenge.finance.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.controller.dto.summary.HeaderDTO;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.service.SummaryService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = { SummaryController.class })
@AutoConfigureMockMvc
@Import(DateFormatConfig.class)
class SummaryControllerTest {

	static final String URL_API = "/api/summary";

	@Autowired
	MockMvc mockMvc;

	@MockBean
	SummaryService service;

	@Nested
	@DisplayName("Method GET by MONTH")
	class MethodGetMonth {

		@Test
		@DisplayName("Should find AUGUST")
		public void successFind() throws Exception {

			GroupCategory groupCategory = new GroupCategory(Categoria.ALIMENTACAO, BigDecimal.TEN);

			given(service.findHeaderByMonth(any(Integer.class), any(Month.class)))
					.willReturn(new HeaderDTO(BigDecimal.ONE, BigDecimal.TEN));
			given(service.findGroupCategoryByMonth(any(Integer.class), any(Month.class)))
					.willReturn(Arrays.asList(groupCategory));

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/2022/"+Month.AUGUST)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.balance.expenditure", is(10)))
					.andExpect(jsonPath("$.balance.income", is(1)))
					.andExpect(jsonPath("$.balance.total", is(-9)))
					.andExpect(jsonPath("$.categories", hasSize(1)))
					.andExpect(jsonPath("$.categories[0].category", is("ALIMENTACAO")))
					.andExpect(jsonPath("$.categories[0].amount", is(10)));
			// @formatter:on

		}

		@Test
		@DisplayName("Should return empty content")
		public void shouldReturnEmptyContent() throws Exception {

			given(service.findHeaderByMonth(any(Integer.class), any(Month.class))).willReturn(new HeaderDTO());
			given(service.findGroupCategoryByMonth(any(Integer.class), any(Month.class))).willReturn(Arrays.asList());

			// @formatter:off
	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .get(URL_API+"/2022/"+Month.DECEMBER)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON);
	        
	        mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.balance.expenditure", is(0)))
					.andExpect(jsonPath("$.balance.income", is(0)))
					.andExpect(jsonPath("$.balance.total", is(0)))
					.andExpect(jsonPath("$.categories", empty()));
			// @formatter:on

		}

	}

	public static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
