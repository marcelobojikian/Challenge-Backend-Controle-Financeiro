package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.model.QExpenditure;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ExpenditureRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	ExpenditureRepository repository;

	@Test
	@DisplayName("Should persist entity.")
	public void successWhenPersistEntity() {

		int sizeExpected = 1;

		Expenditure entity = new Expenditure(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		assertThat(all).hasSize(sizeExpected);

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.OUTRAS);

	}

	@Test
	@DisplayName("Should persist entity with category.")
	public void successWhenPersistEntityWithCategory() {

		int sizeExpected = 1;

		Expenditure entity = new Expenditure(null, "Test", BigDecimal.ZERO, parse("03/08/2022"), Categoria.ALIMENTACAO);
		entityManager.persist(entity);

		List<Expenditure> all = repository.findAll();

		assertThat(all).hasSize(sizeExpected);

		Expenditure entityDB = all.iterator().next();

		assertThat(entityDB.getCategoria()).isEqualTo(Categoria.ALIMENTACAO);

	}

	@Test
	@DisplayName("Should return entities when find by descricao and between dates.")
	public void successWhenFindByDescricaoAndBetweenDates() {

		int sizeExpected = 1;

		Expenditure entity = new Expenditure(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		LocalDate start = parse("03/08/2022").minusDays(1);
		LocalDate end = parse("03/08/2022").plusDays(1);

		List<Expenditure> all = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);

		assertThat(all).hasSize(sizeExpected);

	}

	@Test
	@DisplayName("Should return entities when find by predicate.")
	public void successWhenFindByPredicate() {

		Expenditure entity = new Expenditure(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);
		Expenditure secondEntity = new Expenditure(null, "Test2", BigDecimal.TEN, parse("01/02/2022"));
		entityManager.persist(secondEntity);

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

	@Test
	@DisplayName("Should return entities empty list when find by descricao and between dates.")
	public void returnEmptyWhenFindByDescricaoAndBetweenDates() {

		Expenditure entity = new Expenditure(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		LocalDate start = parse("03/08/2022").plusDays(1);
		LocalDate end = parse("03/08/2022").plusDays(5);

		List<Expenditure> all = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);

		assertThat(all).isEmpty();

	}

	LocalDate parse(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
