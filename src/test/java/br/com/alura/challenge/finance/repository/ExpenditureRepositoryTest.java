package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
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

import br.com.alura.challenge.finance.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.model.QExpenditure;
import br.com.alura.challenge.finance.parameter.FinanceConverter;
import br.com.alura.challenge.finance.parameter.ListFinanceConverter;

@DataJpaTest
class ExpenditureRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	ExpenditureRepository repository;

	@DisplayName("Should persist with Category default")
	@ParameterizedTest
	@CsvSource({ "expenditure::Test:0:03/08/2022", "expenditure::Test:0:03/08/2022:OUTRAS" })
	public void successWhenPersistEntity(@ConvertWith(FinanceConverter.class) Expenditure entity) {
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.OUTRAS);

	}

	@DisplayName("Should persist with Category customized")
	@ParameterizedTest
	@CsvSource({ "expenditure::Test:0:03/08/2022:ALIMENTACAO" })
	public void successWhenPersistEntityWithCategory(@ConvertWith(FinanceConverter.class) Expenditure entity) {
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.ALIMENTACAO);

	}

	@DisplayName("Should return categories list when find between dates.")
	@ParameterizedTest
	@CsvSource({
			"expenditure::Test:1:03/08/2022:LAZER;expenditure::Test 2:10:06/08/2022:LAZER;expenditure::Test 3:10:09/08/2022:ALIMENTACAO;expenditure::Test 4:1:09/09/2022" })
	public void returnCategoriesWhenFindBetweenDates(
			@ConvertWith(ListFinanceConverter.class) List<FinanceEntity> entities) {
		for (FinanceEntity finance : entities) {
			entityManager.persist(finance);
		}

		YearMonth yearMonth = YearMonth.of(2022, Month.AUGUST);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		List<GroupCategory> all = repository.findGroupCategoryBetweenDate(start, end);

		assertThat(all).isNotEmpty();
		assertThat(all).hasSize(2);

		Iterator<GroupCategory> it = all.iterator();

		GroupCategory next = it.next();
		assertThat(next.getCategory()).isEqualTo(Categoria.ALIMENTACAO);
		assertThat(next.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));

		next = it.next();
		assertThat(next.getCategory()).isEqualTo(Categoria.LAZER);
		assertThat(next.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(11));

	}

	@DisplayName("Should return entities when find by predicate.")
	@ParameterizedTest
	@CsvSource({ "expenditure::Test:0:03/08/2022;expenditure::Test2:10:01/02/2022" })
	public void successWhenFindByPredicate(@ConvertWith(ListFinanceConverter.class) List<FinanceEntity> entities) {
		for (FinanceEntity finance : entities) {
			entityManager.persist(finance);
		}

		QExpenditure qExpenditure = QExpenditure.expenditure;
		Predicate predicate = new BooleanBuilder();

		Iterable<Expenditure> result = repository.findAll(predicate);

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
