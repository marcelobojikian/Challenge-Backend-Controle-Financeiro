package br.com.alura.challenge.finance.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.backend.controller.dto.summary.HeaderDTO;
import br.com.alura.challenge.finance.backend.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.backend.repository.ExpenditureRepository;
import br.com.alura.challenge.finance.backend.repository.IncomeRepository;

@Tag("service")
@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

	@InjectMocks
	SummaryService service;

	@Mock
	IncomeRepository incomeRepository;

	@Mock
	ExpenditureRepository expenditureRepository;

	@Nested
	@DisplayName("Find Group Category")
	class FindGroupCategory {

		@Test
		@DisplayName("Should return by month ")
		public void shouldReturnByMonth() {

			GroupCategory groupCategoryExpected = new GroupCategory(Categoria.ALIMENTACAO, BigDecimal.ONE);

			given(expenditureRepository.findGroupCategoryBetweenDate(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(Arrays.asList(groupCategoryExpected));

			List<GroupCategory> groupCategory = service.findGroupCategoryByMonth(2022, Month.APRIL);

			assertThat(groupCategory).isNotEmpty();
			assertThat(groupCategory).hasSize(1);

			GroupCategory next = groupCategory.iterator().next();

			assertThat(next.getCategory()).isEqualByComparingTo(groupCategoryExpected.getCategory());
			assertThat(next.getAmount()).isEqualByComparingTo(groupCategoryExpected.getAmount());

		}

		@Test
		@DisplayName("Should return Empty by month ")
		public void shouldReturnEmptyByMonth() {

			given(expenditureRepository.findGroupCategoryBetweenDate(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(Arrays.asList());

			List<GroupCategory> groupCategory = service.findGroupCategoryByMonth(2022, Month.APRIL);

			assertThat(groupCategory).isEmpty();

		}

	}

	@Nested
	@DisplayName("Find Header")
	class FindHeader {

		@Test
		@DisplayName("Should return empty by month")
		public void shouldReturnEmptyByMonth() {

			HeaderDTO header = service.findHeaderByMonth(2022, Month.APRIL);

			assertThat(header).isNotNull();
			assertThat(header.getIncome()).isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(header.getExpenditure()).isEqualByComparingTo(BigDecimal.ZERO);
			assertThat(header.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);

		}

		@Test
		@DisplayName("Should return by month")
		public void shouldReturnByMonth() {

			BigDecimal totalExpected = BigDecimal.TEN.subtract(BigDecimal.ONE);

			given(incomeRepository.getAmountBetweenDate(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(BigDecimal.TEN);
			given(expenditureRepository.getAmountBetweenDate(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(BigDecimal.ONE);

			HeaderDTO header = service.findHeaderByMonth(2022, Month.APRIL);

			assertThat(header).isNotNull();
			assertThat(header.getIncome()).isEqualByComparingTo(BigDecimal.TEN);
			assertThat(header.getExpenditure()).isEqualByComparingTo(BigDecimal.ONE);
			assertThat(header.getTotal()).isEqualByComparingTo(totalExpected);

		}

	}

}
