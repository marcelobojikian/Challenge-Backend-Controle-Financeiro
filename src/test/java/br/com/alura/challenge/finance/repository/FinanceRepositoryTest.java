package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.model.Income;

class FinanceParams {

	static Stream<FinanceEntity> persist() {
		return Stream.of(new Expenditure(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")),
				new Income(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")));
	}

	static Stream<List<FinanceEntity>> filterDescDate() {
		return Stream.of(Arrays.asList(new Expenditure(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")),
				new Expenditure(null, "Test 2", BigDecimal.ZERO, toDate("12/08/2022")),
				new Expenditure(null, "Test 3", BigDecimal.ZERO, toDate("23/12/2022")),
				new Income(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")),
				new Income(null, "Test 2", BigDecimal.TEN, toDate("12/08/2022")),
				new Income(null, "Test 3", BigDecimal.TEN, toDate("11/08/2022"))));
	}

	static Stream<List<FinanceEntity>> amountFilterDate() {
		return Stream.of(Arrays.asList(new Expenditure(null, "Test", BigDecimal.ONE, toDate("03/08/2022")),
				new Expenditure(null, "Test 2", BigDecimal.TEN, toDate("06/08/2022")),
				new Expenditure(null, "Test 3", BigDecimal.TEN, toDate("09/08/2022")),
				new Expenditure(null, "Test 4", BigDecimal.ONE, toDate("09/09/2022")),
				new Income(null, "Test", BigDecimal.ONE, toDate("03/08/2022")),
				new Income(null, "Test 2", BigDecimal.TEN, toDate("06/08/2022")),
				new Income(null, "Test 3", BigDecimal.TEN, toDate("09/08/2022")),
				new Income(null, "Test 4", BigDecimal.ONE, toDate("09/09/2022"))));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

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
	@MethodSource("br.com.alura.challenge.finance.repository.FinanceParams#persist")
	public void shouldPersistFinance(FinanceEntity entity) {
		FinanceEntity persist = entityManager.persist(entity);
		assertThat(persist).isNotNull();
		assertThat(persist.getId()).isNotNull();
	}

	@DisplayName("Should filter by description and between dates.")
	@ParameterizedTest
	@MethodSource("br.com.alura.challenge.finance.repository.FinanceParams#filterDescDate")
	public void shouldFilterByDescriptionAndBetweenDates(List<FinanceEntity> entities) {
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
	@MethodSource("br.com.alura.challenge.finance.repository.FinanceParams#amountFilterDate")
	public void shouldReturnAmountWithFilterByBetweenDates(List<FinanceEntity> entities) {
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
