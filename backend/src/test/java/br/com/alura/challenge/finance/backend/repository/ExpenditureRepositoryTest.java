package br.com.alura.challenge.finance.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
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

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.backend.rest.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.model.QExpenditure;

class ExpenditureParams {

	static Stream<Expenditure> categoryDefault() {
		return Stream.of(new Expenditure(null, "Test", BigDecimal.ZERO, toDate("03/08/2022")),
				new Expenditure(null, "Test 2", BigDecimal.ZERO, toDate("03/08/2022"), Categoria.OUTRAS));
	}

	static Stream<Expenditure> categoryCustomized() {
		return Stream.of(new Expenditure(null, "Test", BigDecimal.ZERO, toDate("03/08/2022"), Categoria.ALIMENTACAO));
	}

	static Stream<List<Expenditure>> betweenDate() {
		return Stream
				.of(Arrays.asList(new Expenditure(null, "Test", BigDecimal.ONE, toDate("03/08/2022"), Categoria.LAZER),
						new Expenditure(null, "Test 2", BigDecimal.TEN, toDate("06/08/2022"), Categoria.LAZER),
						new Expenditure(null, "Test 3", BigDecimal.TEN, toDate("09/08/2022"), Categoria.ALIMENTACAO),
						new Expenditure(null, "Test 4", BigDecimal.TEN, toDate("09/09/2022"))));
	}

	static Stream<List<Expenditure>> findPredicate() {
		return Stream.of(Arrays.asList(new Expenditure(null, "Test", BigDecimal.ONE, toDate("03/08/2022")),
				new Expenditure(null, "Test 2", BigDecimal.TEN, toDate("01/02/2022"))));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@DataJpaTest
@Tag("repository")
public class ExpenditureRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	ExpenditureRepository repository;

	@DisplayName("Should persist with Category default")
	@ParameterizedTest
	@MethodSource("br.com.alura.challenge.finance.backend.repository.ExpenditureParams#categoryDefault")
	public void successWhenPersistEntity(Expenditure entity) {
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.OUTRAS);

	}

	@DisplayName("Should persist with Category customized")
	@ParameterizedTest
	@MethodSource("br.com.alura.challenge.finance.backend.repository.ExpenditureParams#categoryCustomized")
	public void successWhenPersistEntityWithCategory(Expenditure entity) {
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.ALIMENTACAO);

	}

	@DisplayName("Should return categories list when find between dates.")
	@ParameterizedTest
	@MethodSource("br.com.alura.challenge.finance.backend.repository.ExpenditureParams#betweenDate")
	public void returnCategoriesWhenFindBetweenDates(List<FinanceEntity> entities) {
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
	@MethodSource("br.com.alura.challenge.finance.backend.repository.ExpenditureParams#findPredicate")
	public void successWhenFindByPredicate(List<FinanceEntity> entities) {
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

		predicate = qExpenditure.data.between(ExpenditureParams.toDate("01/01/2022"),
				ExpenditureParams.toDate("01/12/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 2;
		assertThat(result).hasSize(sizeExpected);

		predicate = qExpenditure.data.between(ExpenditureParams.toDate("01/01/2022"),
				ExpenditureParams.toDate("01/04/2022"));
		result = repository.findAll(predicate);

		sizeExpected = 1;
		assertThat(result).hasSize(sizeExpected);

	}

}
