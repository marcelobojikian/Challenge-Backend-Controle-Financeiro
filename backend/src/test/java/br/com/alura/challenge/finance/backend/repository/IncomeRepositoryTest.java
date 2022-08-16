package br.com.alura.challenge.finance.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.model.QIncome;

class IncomeParams {

	static Stream<List<Income>> findPredicate() {
		return Stream.of(Arrays.asList(new Income(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")),
				new Income(null, "Test 2", BigDecimal.TEN, toDate("01/02/2022"))));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@DataJpaTest
@Tag("repository")
class IncomeRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	IncomeRepository repository;

	@DisplayName("Should return entities when find by predicate.")
	@ParameterizedTest
	@MethodSource("br.com.alura.challenge.finance.backend.repository.IncomeParams#findPredicate")
	public void successWhenFindByPredicate(List<FinanceEntity> entities) {
		for (FinanceEntity finance : entities) {
			entityManager.persist(finance);
		}

		QIncome qExpenditure = QIncome.income;
		Predicate predicate = new BooleanBuilder();

		Iterable<Income> result = repository.findAll(predicate);

		int sizeExpected = 2;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.descricao.contains("2");
		result = repository.findAll(predicate);

		sizeExpected = 1;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.valor.between(5, 15);
		result = repository.findAll(predicate);

		sizeExpected = 1;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.data.between(IncomeParams.toDate("01/01/2022"), IncomeParams.toDate("01/12/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 2;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.data.between(IncomeParams.toDate("01/01/2022"), IncomeParams.toDate("01/04/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 1;
		assertThat(result).hasSize(sizeExpected);

	}

}
