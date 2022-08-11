package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.model.QIncome;
import br.com.alura.challenge.finance.parameter.ListFinanceConverter;

@DataJpaTest
class IncomeRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	IncomeRepository repository;

	@DisplayName("Should return entities when find by predicate.")
	@ParameterizedTest
	@CsvSource({ "income::Test:0:03/08/2022;income::Test2:10:01/02/2022" })
	public void successWhenFindByPredicate(@ConvertWith(ListFinanceConverter.class) List<FinanceEntity> entities) {
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

		predicate = qExpenditure.data.between(parse("01/01/2022"), parse("01/12/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 2;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.data.between(parse("01/01/2022"), parse("01/04/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 1;
		assertThat(result).hasSize(sizeExpected);

	}

	LocalDate parse(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
