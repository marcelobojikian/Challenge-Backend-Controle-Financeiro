package br.com.alura.challenge.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		@DisplayName("Should find by id")
		public void shouldFindIncome() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			Income result = incomeService.findById(1L);

			assertThat(result).isEqualTo(expected);

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotFindIncome() {

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

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income result = incomeService.save(entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should create when same name and different month")
		public void shouldCreateWhenSameNameButDifferentMonth() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/09/2022"));

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income result = incomeService.save(entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should throw exception when same name and month")
		public void shouldReturnExceptionWhenIncomeAlreadyExistWithSameMonth() {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(
							Arrays.asList(new Income(1l, "Test", BigDecimal.valueOf(23), toDate("05/08/2022"))));

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			assertThrows(BusinessException.class, () -> {
				incomeService.save(entity);
			});

		}

	}

	@Nested
	@DisplayName("Update Income")
	class UpdateIncoming {

		@Test
		@DisplayName("Should update")
		public void shouldUpdateIncome() {

			Income expected = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));

			Income entityDB = new Income(1l, "Test", BigDecimal.valueOf(24), toDate("03/08/2022"));
			given(repository.findById(any(Long.class))).willReturn(Optional.of(entityDB));
			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));
			Income result = incomeService.update(1l, entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should update when same name and different month")
		public void shouldUpdateWhenSameNameAndDifferentMonth() {
			IncomeService serviceSpy = spy(incomeService);

			Long idExpected = 1l;
			Income expected = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));

			Income entityDB = new Income(1l, "Test", BigDecimal.valueOf(24), toDate("03/07/2022"));
			assertThat(entityDB.isSameMonth(expected)).isFalse();

			given(repository.findById(any(Long.class))).willReturn(Optional.of(entityDB));

			serviceSpy.update(idExpected, expected);

			verify(serviceSpy, times(1)).save(any(Income.class));

		}

		@Test
		@DisplayName("Should throw exception when income not exist")
		public void shouldReturnExceptionWhenIncomeNotExist() {

			given(repository.findById(any(Long.class))).willReturn(Optional.empty());

			Income entity = new Income(-1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			assertThrows(EntityNotFoundException.class, () -> {
				incomeService.update(-1l, entity);
			});

		}

	}

	LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
