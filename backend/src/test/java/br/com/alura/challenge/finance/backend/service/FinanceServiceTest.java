package br.com.alura.challenge.finance.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.repository.FinanceRepository;
import br.com.alura.challenge.finance.backend.rest.exception.BusinessException;
import br.com.alura.challenge.finance.backend.rest.exception.EntityNotFoundException;

abstract class FinanceServiceTest<T extends FinanceEntity> {

	abstract FinanceService<T> getService();

	abstract FinanceRepository<T> getRepository();

	abstract T one();

	abstract T[] list();

	abstract Class<T> instanceClass();

	@Nested
	@DisplayName("Find All ")
	class FindAll {

		@Test
		@DisplayName("Should find all")
		public void shouldFind() {

			T[] expected = list();

			given(getRepository().findAll()).willReturn(Arrays.asList(expected));

			List<T> result = getService().findAll();

			assertThat(result).isNotNull();
			assertThat(result).isNotEmpty();
			assertThat(result).contains(expected);

		}

		@Test
		@DisplayName("Should find all with Predicate")
		public void shouldFindWithPredicate() {

			T[] expected = list();

			given(getRepository().findAll(any(Predicate.class))).willReturn(Arrays.asList(expected));

			Iterable<T> result = getService().findAll(new BooleanBuilder());

			assertThat(result).isNotNull();
			assertThat(result).isNotEmpty();
			assertThat(result).contains(expected);

		}

		@Test
		@DisplayName("Should find empty list")
		public void shouldNotFind() {

			given(getRepository().findAll()).willReturn(Arrays.asList());
			List<T> result = getService().findAll();

			assertThat(result).isNotNull();
			assertThat(result).isEmpty();

		}

	}

	@Nested
	@DisplayName("Find by ID")
	class FindById {

		@Test
		@DisplayName("Should find by id")
		public void shouldFind() {

			T expected = one();

			given(getRepository().findById(anyLong())).willReturn(Optional.of(expected));

			T result = getService().findById(1L);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(expected);

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotFind() {

			given(getRepository().findById(anyLong())).willReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				getService().findById(1L);
			});

		}

	}

	@Nested
	@DisplayName("Find between Dates")
	class FindBetweenDates {

		@Test
		@DisplayName("Should find all")
		public void shouldFind() {

			T[] expected = list();

			given(getRepository().findAllByDataBetween(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(Arrays.asList(expected));

			Iterable<T> result = getService().findBetweenDate(LocalDate.now(), LocalDate.now());

			assertThat(result).isNotNull();
			assertThat(result).isNotEmpty();
			assertThat(result).contains(expected);

		}

		@Test
		@DisplayName("Should find empty list")
		public void shouldNotFind() {

			given(getRepository().findAllByDataBetween(any(LocalDate.class), any(LocalDate.class)))
					.willReturn(Arrays.asList());
			Iterable<T> result = getService().findBetweenDate(LocalDate.now(), LocalDate.now());

			assertThat(result).isNotNull();
			assertThat(result).isEmpty();

		}

	}

	@Nested
	@DisplayName("Create")
	class Create {

		@Test
		@DisplayName("Should create")
		public void shouldCreate() {

			T expected = one();

			given(getRepository().findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(getRepository().save(any(instanceClass()))).willReturn(expected);

			T result = getService().save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should create when same name and different month")
		public void shouldCreateWhenSameNameButDifferentMonth() {

			T expected = one();

			given(getRepository().findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList());
			given(getRepository().save(any(instanceClass()))).willReturn(expected);

			T result = getService().save(expected);

			assertThat(result.getId()).isEqualTo(expected.getId());
			assertThat(result.getDescricao()).isEqualTo(expected.getDescricao());
			assertThat(result.getValor()).isEqualTo(expected.getValor());
			assertThat(result.getData()).isEqualTo(expected.getData());

		}

		@Test
		@DisplayName("Should throw exception when same name and month")
		public void shouldReturnExceptionWhenAlreadyExistWithSameMonth() {

			T expected = one();

			given(getRepository().findAllByDescricaoContainingIgnoreCaseAndDataBetween(any(String.class),
					any(LocalDate.class), any(LocalDate.class))).willReturn(Arrays.asList(expected));

			assertThrows(BusinessException.class, () -> {
				getService().save(expected);
			});

		}

	}

	@Nested
	@DisplayName("Delete")
	class Delete {

		@Test
		@DisplayName("Should delete")
		public void shouldDelete() {

			T expected = one();

			given(getRepository().findById(anyLong())).willReturn(Optional.of(expected));

			getService().delete(1L);

			verify(getRepository(), times(1)).delete(any(instanceClass()));

		}

		@Test
		@DisplayName("Should throw exception")
		public void shouldNotDelete() {

			given(getRepository().findById(anyLong())).willReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> {
				getService().delete(1L);
			});

		}

	}

	LocalDate toDate(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
