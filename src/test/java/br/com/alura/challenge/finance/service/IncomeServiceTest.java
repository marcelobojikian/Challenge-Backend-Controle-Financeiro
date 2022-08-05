package br.com.alura.challenge.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

	@InjectMocks
	IncomeService incomeService;

	@Mock
	IncomeRepository repository;

	@Nested
	@DisplayName("Find Income by ID")
	class FindByIdIncoming {

		@Test
		@DisplayName("Should find by critery")
		public void shouldFindIncome() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			Income result = incomeService.findById(1L);

			assertThat(result).isEqualTo(expected);

		}

		@Test
		@DisplayName("Should return exception")
		public void shouldCreateIncome() {

			given(repository.findById(anyLong())).willReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				incomeService.findById(1L);
			});

		}

	}

	@Nested
	@DisplayName("Create New Income")
	class CreateIncoming {

		@Test
		@DisplayName("Should create")
		public void shouldCreateIncome() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income result = incomeService.create(entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should create when same name but different month")
		public void shouldCreateWhenSameNameButDifferentMonth() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/09/2022"));

			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income result = incomeService.create(entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should return exception when income already exist with same month")
		public void shouldReturnExceptionWhenIncomeAlreadyExistWithSameMonth() {

			String descricao = "Test";
			LocalDate dateTimeMin = firstDayOfMonth("01/08/2022");
			LocalDate dateTimeMax = lastDayOfMonth("31/08/2022");

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(descricao, dateTimeMin, dateTimeMax))
					.willReturn(Arrays.asList(new Income(1l, descricao, BigDecimal.valueOf(23), toDate("05/08/2022"))));

			Income entity = new Income(null, "     Test ", BigDecimal.valueOf(23), toDate("03/08/2022"));

			assertThrows(BusinessException.class, () -> {
				incomeService.create(entity);
			});

		}

	}

	LocalDate firstDayOfMonth(String date) {
		return dayOfMonth(date, TemporalAdjusters.firstDayOfMonth(), LocalTime.MIN);
	}

	LocalDate lastDayOfMonth(String date) {
		return dayOfMonth(date, TemporalAdjusters.lastDayOfMonth(), LocalTime.MAX);
	}

	LocalDate dayOfMonth(String date, TemporalAdjuster temporalAdjuster, LocalTime localTime) {
		return toDate(date).with(temporalAdjuster);
	}

	LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
