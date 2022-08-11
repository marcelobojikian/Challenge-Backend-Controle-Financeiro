package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.parameter.FinanceConverter;
import br.com.alura.challenge.finance.parameter.ListFinanceConverter;

@DataJpaTest
class FinanceRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	ExpenditureRepository expenditureRepository;

	@Autowired
	IncomeRepository incomeRepository;

	@DisplayName("Should persist FinanceEntity")
	@ParameterizedTest
	@CsvSource({ "expenditure::Test:0:03/08/2022", "income::Test:0:03/08/2022", })
	public void shouldPersistFinance(@ConvertWith(FinanceConverter.class) FinanceEntity entity) {
		FinanceEntity persist = entityManager.persist(entity);
		assertThat(persist).isNotNull();
		assertThat(persist.getId()).isNotNull();
	}

	@DisplayName("Should filter by description and between dates.")
	@ParameterizedTest
	@CsvSource({ "expenditure::Test:0:03/08/2022;expenditure::Test 2:0:12/08/2022;expenditure::Test 3:0:23/12/2022;"
			+ "income::Test:0:03/08/2022;income::Test 2:10:12/08/2022;income::Test 3:0:11/08/2022" })
	public void shouldFilterByDescriptionAndBetweenDates(
			@ConvertWith(ListFinanceConverter.class) List<FinanceEntity> entities) {
		for (FinanceEntity finance : entities) {
			entityManager.persist(finance);
		}

		YearMonth yearMonth = YearMonth.of(2022, Month.AUGUST);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<Expenditure> expenditures = expenditureRepository
				.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);
		assertThat(expenditures).isNotEmpty();

		List<Income> incomes = incomeRepository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start,
				end);
		assertThat(incomes).isNotEmpty();

	}

	@Test
	@DisplayName("Should return empty when filter by description and between dates.")
	public void shouldReturnEmptyWhenFilterByDescriptionAndBetweenDates() {

		YearMonth yearMonth = YearMonth.of(2022, Month.AUGUST);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<Expenditure> expenditures = expenditureRepository
				.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);
		assertThat(expenditures).isEmpty();

		List<Income> incomes = incomeRepository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start,
				end);
		assertThat(incomes).isEmpty();

	}

	@DisplayName("Should return amount with filter by between dates.")
	@ParameterizedTest
	@CsvSource({
			"expenditure::Test:1:03/08/2022;expenditure::Test 2:10:06/08/2022;expenditure::Test 3:10:09/08/2022;expenditure::Test 4:1:09/09/2022;"
					+ "income::Test:1:03/08/2022;income::Test 2:10:06/08/2022;income::Test 3:10:09/08/2022;income::Test 4:1:09/09/2022" })
	public void shouldReturnAmountWithFilterByBetweenDates(
			@ConvertWith(ListFinanceConverter.class) List<FinanceEntity> entities) {
		for (FinanceEntity finance : entities) {
			entityManager.persist(finance);
		}

		YearMonth yearMonth = YearMonth.of(2022, Month.AUGUST);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		BigDecimal amount = expenditureRepository.getAmountBetweenDate(start, end);
		assertThat(amount).isEqualByComparingTo(BigDecimal.valueOf(21));

		amount = incomeRepository.getAmountBetweenDate(start, end);
		assertThat(amount).isEqualByComparingTo(BigDecimal.valueOf(21));

	}

}
