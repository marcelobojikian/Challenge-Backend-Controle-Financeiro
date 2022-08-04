package br.com.alura.challenge.finance.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

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

import br.com.alura.challenge.finance.model.Income;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class IncomeRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	IncomeRepository repository;

	@Test
	@DisplayName("Should persist entity.")
	public void successWhenPersistEntity() {

		int sizeExpected = 1;

		Income entity = new Income(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		List<Income> all = repository.findAll();

		assertSame(sizeExpected, all.size());

	}

	@Test
	@DisplayName("Should return entities when find by descricao and between dates.")
	public void successWhenFindByDescricaoAndBetweenDates() {

		int sizeExpected = 1;

		Income entity = new Income(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		LocalDate start = parse("03/08/2022").minusDays(1);
		LocalDate end = parse("03/08/2022").plusDays(1);

		List<Income> all = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);

		assertThat(all).hasSize(sizeExpected);

	}

	@Test
	@DisplayName("Should return entities empty list when find by descricao and between dates.")
	public void returnEmptyWhenFindByDescricaoAndBetweenDates() {

		Income entity = new Income(null, "Test", BigDecimal.ZERO, parse("03/08/2022"));
		entityManager.persist(entity);

		LocalDate start = parse("03/08/2022").plusDays(1);
		LocalDate end = parse("03/08/2022").plusDays(5);

		List<Income> all = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween("Test", start, end);

		assertThat(all).isEmpty();

	}

	LocalDate parse(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
