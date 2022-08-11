package br.com.alura.challenge.finance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.repository.ExpenditureRepository;

class ExpenditureParams {

	static Stream<List<Expenditure>> list() {
		return Stream.of(Arrays.asList(new Expenditure(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")),
				new Expenditure(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022"))));
	}

	static Stream<Expenditure> one() {
		return Stream.of(new Expenditure(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest {

	@InjectMocks
	ExpenditureService service;

	@Mock
	ExpenditureRepository repository;

	@Nested
	@DisplayName("Find All")
	class FindAll {

		@DisplayName("Should find all")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#list")
		public void shouldFind(List<Expenditure> expected) {

			given(repository.findAll()).willReturn(expected);

			List<Expenditure> result = service.findAll();

			assertThat(result).contains(expected.toArray(new Expenditure[expected.size()]));

		}

		@DisplayName("Should find all with Predicate")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#list")
		public void shouldFindWithPredicate(List<Expenditure> expected) {

			given(repository.findAll(any(Predicate.class))).willReturn(expected);

			Iterable<Expenditure> result = service.findAll(new BooleanBuilder());

			assertThat(result).contains(expected.toArray(new Expenditure[expected.size()]));

		}

		@Test
		@DisplayName("Should find empty list")
		public void shouldNotFind() {

			given(repository.findAll()).willReturn(Arrays.asList());
			List<Expenditure> result = service.findAll();

			assertThat(result).isEmpty();

		}

	}

	@Nested
	@DisplayName("Find by ID")
	class FindById {

		@DisplayName("Should find by id")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#one")
		public void shouldFind(Expenditure expected) {

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			Expenditure result = service.findById(1L);

			assertThat(result).isEqualTo(expected);

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotFind() {

			given(repository.findById(anyLong())).willReturn(Optional.empty());

			Exception exception = assertThrows(EntityNotFoundException.class, () -> {
				service.findById(1L);
			});

			String expectedMessage = "Entity not found";
			String actualMessage = exception.getMessage();

			assertTrue(actualMessage.contains(expectedMessage));

		}

	}

	@Nested
	@DisplayName("Create")
	class Create {

		@DisplayName("Should create")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#one")
		public void shouldCreate(Expenditure expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Expenditure.class))).willReturn(expected);

			Expenditure result = service.save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());
			assertThat(result.getCategoria()).isEqualTo(Categoria.OUTRAS);

		}

		@DisplayName("Should create when same name and different month")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#one")
		public void shouldCreateWhenSameNameAndDifferentMonth(Expenditure expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(repository.save(any(Expenditure.class))).willReturn(expected);

			Expenditure result = service.save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());
			assertThat(result.getCategoria()).isEqualTo(Categoria.OUTRAS);

		}

		@DisplayName("Should throw exception when same name and month")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#one")
		public void shouldReturnExceptionWhenAlreadyExistWithSameMonth(Expenditure expected) {

			given(repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList(expected));

			Exception exception = assertThrows(BusinessException.class, () -> {
				service.save(expected);
			});

			String expectedMessage = "There is this finance for this month";
			String actualMessage = exception.getMessage();

			assertTrue(actualMessage.contains(expectedMessage));

		}

	}

	@Nested
	@DisplayName("Delete")
	class Delete {

		@DisplayName("Should delete")
		@ParameterizedTest
		@MethodSource("br.com.alura.challenge.finance.service.ExpenditureParams#one")
		public void shouldDelete(Expenditure expected) {

			given(repository.findById(anyLong())).willReturn(Optional.of(expected));

			service.delete(1L);

			verify(repository, times(1)).delete(any(Expenditure.class));

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotDelete() {

			given(repository.findById(anyLong())).willReturn(Optional.empty());

			Exception exception = assertThrows(EntityNotFoundException.class, () -> {
				service.delete(1L);
			});

			String expectedMessage = "Entity not found";
			String actualMessage = exception.getMessage();

			assertTrue(actualMessage.contains(expectedMessage));

		}

	}

}
