package br.com.alura.challenge.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

class IncomeParams {

	static Stream<List<Income>> list() {
		return Stream.of(Arrays.asList(new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")),
				new Income(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"))));
	}

	static Stream<Income> one() {
		return Stream.of(new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {

	@InjectMocks
	IncomeService service;

	@Mock
	IncomeRepository repository;

	@Nested
	@DisplayName("Find All ")
	class FindAll {

		@DisplayName("Should find all")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#list")
		public void shouldFind(List<Income> expected) {

			given(repository.findAll()).willReturn(expected);

			List<Income> result = service.findAll();

			assertThat(result).contains(expected.toArray(new Income[expected.size()]));

		}

		@DisplayName("Should find all with Predicate")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#list")
		public void shouldFindWithPredicate(List<Income> expected) {

			given(repository.findAll(any(Predicate.class))).willReturn(expected);

			Iterable<Income> result = service.findAll(new BooleanBuilder());

			assertThat(result).contains(expected.toArray(new Income[expected.size()]));

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

		@DisplayName("Should find by id")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#one")
		public void shouldFind(Income expected) {

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

		@DisplayName("Should create")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#one")
		public void shouldCreate(Income expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Income.class))).willReturn(expected);

			Income result = service.save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@DisplayName("Should create when same name and different month")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#one")
		public void shouldCreateWhenSameNameButDifferentMonth(Income expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Income.class))).willReturn(expected);

			Income result = service.save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@DisplayName("Should throw exception when same name and month")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#one")
		public void shouldReturnExceptionWhenAlreadyExistWithSameMonth(Income expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList(expected));

			assertThrows(BusinessException.class, () -> {
				service.save(expected);
			});

		}

	}

	@Nested
	@DisplayName("Delete")
	class Delete {

		@DisplayName("Should delete")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.IncomeParams#one")
		public void shouldDelete(Income expected) {

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

}
