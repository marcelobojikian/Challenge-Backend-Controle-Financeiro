package br.com.alura.challenge.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@ExtendWith(SpringExtension.class)
class IncomeServiceTest {

	IncomeService incomeService;

	@MockBean
	IncomeRepository repository;

	@BeforeEach
	public void setUp() {
		this.incomeService = new IncomeService(repository);
	}

	@Nested
	@DisplayName("Create New Income")
	class CreateIncoming {

		@Test
		@DisplayName("Should create")
		public void shouldCreateIncome() {

			Income entityExpected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(false);
			BDDMockito.given(repository.save(Mockito.any(Income.class))).willReturn(entityExpected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income savedEntity = incomeService.create(entity);

			assertThat(savedEntity.getId()).isEqualTo(1);
			assertThat(savedEntity.getDescricao()).isEqualTo("Test");
			assertThat(savedEntity.getValor()).isEqualTo("23");
			assertThat(savedEntity.getData()).isEqualTo(toDate("03/08/2022"));

		}

		@Test
		@DisplayName("Should create when same name but different month")
		public void shouldCreateWhenSameNameButDifferentMonth() {

			Income entityExpected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/09/2022"));

			Mockito.when(repository.existsById(Mockito.anyLong())).thenReturn(false);
			BDDMockito.given(repository.save(Mockito.any(Income.class))).willReturn(entityExpected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income savedEntity = incomeService.create(entity);

			assertThat(savedEntity.getId()).isEqualTo(1);
			assertThat(savedEntity.getDescricao()).isEqualTo("Test");
			assertThat(savedEntity.getValor()).isEqualTo("23");
			assertThat(savedEntity.getData()).isEqualTo(toDate("03/09/2022"));

		}

		@Test
		@DisplayName("Should return exception when income already exist with same month")
		public void shouldReturnExceptionWhenIncomeAlreadyExistWithSameMonth() {

			LocalDate dateTimeMin = firstDayOfMonth("01/08/2022");
			LocalDate dateTimeMax = lastDayOfMonth("31/08/2022");

			BDDMockito
					.given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", dateTimeMin,
							dateTimeMax))
					.willReturn(Arrays.asList(new Income(1l, "Test", BigDecimal.valueOf(23), toDate("05/08/2022"))));

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
