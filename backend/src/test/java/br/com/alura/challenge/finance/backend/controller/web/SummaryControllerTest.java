package br.com.alura.challenge.finance.backend.controller.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.alura.challenge.finance.backend.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.backend.controller.dto.summary.HeaderDTO;
import br.com.alura.challenge.finance.backend.controller.dto.summary.SummaryDTO;
import br.com.alura.challenge.finance.backend.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.backend.service.SummaryService;

@ExtendWith(MockitoExtension.class)
class SummaryControllerTest {

	@InjectMocks
	SummaryController controller;

	@Mock
	SummaryService service;

	@Nested
	@DisplayName("Method GET by MONTH")
	class MethodGetByMonth {

		@Test
		void shouldGetWithCategorias() {
			
			HeaderDTO headerExpected = new HeaderDTO(BigDecimal.ONE, BigDecimal.TEN);
			GroupCategory groupCategoryExpected = new GroupCategory(Categoria.ALIMENTACAO, BigDecimal.TEN);
			
			List<GroupCategory> listGroupCategoriaExpected = Arrays.asList(groupCategoryExpected);
			SummaryDTO summaryExpected = new SummaryDTO(headerExpected, listGroupCategoriaExpected);
			
			given(service.findHeaderByMonth(any(Integer.class), any(Month.class))).willReturn(headerExpected);
			given(service.findGroupCategoryByMonth(any(Integer.class), any(Month.class))).willReturn(listGroupCategoriaExpected);

			ResponseEntity<?> result = controller.findByYearAndMonth(2022,Month.AUGUST);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(SummaryDTO.class);
			assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(summaryExpected);
			
		}

		@Test
		void shouldGetWithoutCategorias() {
			
			HeaderDTO headerExpected = new HeaderDTO();
			List<GroupCategory> groupCategoriaExpected = Arrays.asList();
			
			SummaryDTO summaryExpected = new SummaryDTO(headerExpected, groupCategoriaExpected);
			
			given(service.findHeaderByMonth(any(Integer.class), any(Month.class))).willReturn(headerExpected);
			given(service.findGroupCategoryByMonth(any(Integer.class), any(Month.class))).willReturn(groupCategoriaExpected);

			ResponseEntity<?> result = controller.findByYearAndMonth(2022,Month.AUGUST);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(SummaryDTO.class);
			assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(summaryExpected);
			
		}

	}

}
