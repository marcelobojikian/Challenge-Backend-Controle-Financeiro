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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

	@InjectMocks
	IncomeService service;

	@Mock
	IncomeRepository repository;

	@Nested
	@DisplayName("Find All ")
	class FindAll {

		@Test
		@DisplayName("Should find all")
		public void shouldFind() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income secondExpected = new Income(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"));

			given(repository.findAll()).willReturn(Arrays.asList(expected, secondExpected));

			List<Income> result = service.findAll();

			assertThat(result).contains(expected, secondExpected);

		}

		@Test
		@DisplayName("Should find all with Predicate")
		public void shouldFindWithPredicate() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income secondExpected = new Income(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"));

			given(repository.findAll(any(Predicate.class))).willReturn(Arrays.asList(expected, secondExpected));

			Iterable<Income> result = service.findAll(new BooleanBuilder());

			assertThat(result).contains(expected, secondExpected);

		}

		@Test
		@DisplayName("Should find empty list")
		public void shouldNotFind() {

			given(repository.findAll()).willReturn(Arrays.asList());
			List<Income> result = service.findAll();

			assertThat(result).isEmpty();

		}

	}

	@Nested
	@DisplayName("Find by ID")
	class FindById {

		@Test
		@DisplayName("Should find by id")
		public void shouldFind() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			Income result = service.findById(1L);

			assertThat(result).isEqualTo(expected);

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotFind() {

			given(repository.findById(anyLong())).willReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				service.findById(1L);
			});

		}

	}

	@Nested
	@DisplayName("Create")
	class Create {

		@Test
		@DisplayName("Should create")
		public void shouldCreate() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
			Income result = service.save(entity);

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
			Income result = service.save(entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should throw exception when same name and month")
		public void shouldReturnExceptionWhenAlreadyExistWithSameMonth() {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(
							Arrays.asList(new Income(1l, "Test", BigDecimal.valueOf(23), toDate("05/08/2022"))));

			Income entity = new Income(null, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			assertThrows(BusinessException.class, () -> {
				service.save(entity);
			});

		}

	}

	@Nested
	@DisplayName("Update")
	class Update {

		@Test
		@DisplayName("Should update")
		public void shouldUpdate() {

			Income expected = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));

			Income entityDB = new Income(1l, "Test", BigDecimal.valueOf(24), toDate("03/08/2022"));
			given(repository.findById(any(Long.class))).willReturn(Optional.of(entityDB));
			given(repository.save(any(Income.class))).willReturn(expected);

			Income entity = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));
			Income result = service.update(1l, entity);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should update when same name and different month")
		public void shouldUpdateWhenSameNameAndDifferentMonth() {
			IncomeService serviceSpy = spy(service);

			Long idExpected = 1l;
			Income expected = new Income(1l, "Test 2", BigDecimal.valueOf(34), toDate("07/08/2022"));

			Income entityDB = new Income(1l, "Test", BigDecimal.valueOf(24), toDate("03/07/2022"));
			assertThat(entityDB.isSameMonth(expected)).isFalse();

			given(repository.findById(any(Long.class))).willReturn(Optional.of(entityDB));

			serviceSpy.update(idExpected, expected);

			verify(serviceSpy, times(1)).save(any(Income.class));

		}

		@Test
		@DisplayName("Should throw exception when not exist")
		public void shouldReturnExceptionWhenNotExist() {

			given(repository.findById(any(Long.class))).willReturn(Optional.empty());

			Income entity = new Income(-1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			assertThrows(EntityNotFoundException.class, () -> {
				service.update(-1l, entity);
			});

		}

	}

	@Nested
	@DisplayName("Delete")
	class Delete {

		@Test
		@DisplayName("Should delete")
		public void shouldDelete() {

			Income expected = new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			service.delete(1L);

			verify(repository, times(1)).delete(any(Income.class));

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotDelete() {

			given(repository.findById(anyLong())).willReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				service.delete(1L);
			});

		}

	}

	LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
