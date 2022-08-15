package br.com.alura.challenge.finance.backend.controller.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.backend.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.backend.exception.BusinessException;
import br.com.alura.challenge.finance.backend.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.backend.model.FinanceEntity;

abstract class FinanceControllerTest<T extends FinanceEntity, DTO extends FinanceDTO> {

	abstract DTO getInstanceDto();

	abstract Class<DTO> getDtoCLass();

	abstract String getCollectionRelationName();

	abstract FinanceController<T, DTO> getController();

	abstract FinanceControllerManager<T, DTO> getManager();

	@Nested
	@DisplayName("Method GET ONE")
	class MethodGetOne {

		@Test
		void shouldGetOneOk() {

			DTO entity = getInstanceDto();
			EntityModel<DTO> model = model(entity);
			given(getManager().findById(anyLong())).willReturn(model);

			ResponseEntity<?> result = getController().one(1l);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(EntityModel.class);
			assertThat(result.getBody()).isEqualTo(model);
			
		}

		@Test
		void shouldGetOneNotFound() {

			given(getManager().findById(anyLong())).willThrow(EntityNotFoundException.class);

			assertThrows(EntityNotFoundException.class, () -> {
				getController().one(1l);
			});
			
		}

	}

	@Nested
	@DisplayName("Method GET All")
	class MethodGetAll {

		@Test
		void shouldGetAllOk() {

			DTO entity = getInstanceDto();
			List<DTO> entities = Arrays.asList(entity);
			CollectionModel<EntityModel<DTO>> collectionModel = collectionModel(entities);
			given(getManager().findAll(any(Predicate.class))).willReturn(collectionModel);

			ResponseEntity<?> result = getController().all(new BooleanBuilder());

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(CollectionModel.class);
			assertThat(result.getBody()).isEqualTo(collectionModel);

		}

		@Test
		void shouldGetAllNoContent() {
			
			CollectionModel<EntityModel<DTO>> collectionModel = collectionModel(Arrays.asList());

			given(getManager().findAll(any(Predicate.class))).willReturn(collectionModel);

			ResponseEntity<?> result = getController().all(new BooleanBuilder());

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_NO_CONTENT);
			assertThat(result.getBody()).isNull();

		}

	}

	@Nested
	@DisplayName("Method GET MONTH")
	class MethodGetMonth {

		@Test
		void shouldGetByYearAndMonthOk() {

			DTO entity = getInstanceDto();
			List<DTO> entities = Arrays.asList(entity);
			CollectionModel<EntityModel<DTO>> collectionModel = collectionModel(entities);
			given(getManager().allWithYearAndMonth(any(Integer.class), any(Month.class)))
					.willReturn(collectionModel);

			ResponseEntity<?> result = getController().byYearAndMonth(2022, Month.AUGUST);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(CollectionModel.class);
			assertThat(result.getBody()).isEqualTo(collectionModel);

		}

		@Test
		void shouldGetByYearAndMonthNoContent() {
			
			CollectionModel<EntityModel<DTO>> collectionModel = collectionModel(Arrays.asList());

			given(getManager().allWithYearAndMonth(any(Integer.class), any(Month.class)))
					.willReturn(collectionModel);

			ResponseEntity<?> result = getController().byYearAndMonth(2022, Month.JANUARY);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_NO_CONTENT);
			assertThat(result.getBody()).isNull();
			
		}

	}

	@Nested
	@DisplayName("Method POST")
	class MethodPost {

		@Test
		void shouldPostCreated() {

			DTO entity = getInstanceDto();
			EntityModel<DTO> model = model(entity);
			given(getManager().create(any(getDtoCLass()))).willReturn(model);

			ResponseEntity<?> result = getController().createFinance(getInstanceDto());
			
			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_CREATED);
			assertThat(result.getHeaders()).containsKey(HttpHeaders.LOCATION);
			assertThat(result.getBody()).isInstanceOf(EntityModel.class);
			assertThat(result.getBody()).isEqualTo(model);
			
		}

		@Test
		void shouldPostBadRequest() {

			given(getManager().create(any(getDtoCLass()))).willThrow(EntityNotFoundException.class);

			assertThrows(EntityNotFoundException.class, () -> {
				getController().createFinance(getInstanceDto());
			});

		}

	}

	@Nested
	@DisplayName("Method PUT")
	class MethodPut {

		@Test
		void shouldPutOk() {

			DTO entity = getInstanceDto();
			EntityModel<DTO> model = model(entity);
			given(getManager().update(any(Long.class), any(getDtoCLass()))).willReturn(model);

			ResponseEntity<?> result = getController().updateFinance(1l, getInstanceDto());

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);
			assertThat(result.getBody()).isInstanceOf(EntityModel.class);
			assertThat(result.getBody()).isEqualTo(model);

		}

		@Test
		void shouldPutNotFound() {

			given(getManager().update(any(Long.class), any(getDtoCLass()))).willThrow(EntityNotFoundException.class);

			assertThrows(EntityNotFoundException.class, () -> {
				getController().updateFinance(1l, getInstanceDto());
			});

		}

		@Test
		void shouldPostBadRequestAlreadyMonth() {

			given(getManager().update(any(Long.class), any(getDtoCLass()))).willThrow(BusinessException.class);

			assertThrows(BusinessException.class, () -> {
				getController().updateFinance(1l, getInstanceDto());
			});

		}
	}

	@Nested
	@DisplayName("Method DELETE")
	class MethodDelete {

		@Test
		void shouldDeleteOk() {

			ResponseEntity<?> result = getController().deleteFinance(1l);

			assertThat(result.getStatusCodeValue()).isEqualTo(HttpStatus.SC_OK);

		}

		@Test
		void shouldDeleteNotFound() {

			willThrow(EntityNotFoundException.class).given(getManager()).remove(any(Long.class));

			assertThrows(EntityNotFoundException.class, () -> {
				getController().deleteFinance(1l);
			});
			
		}

	}

	public CollectionModel<EntityModel<DTO>> collectionModel(List<DTO> entities) {
		return CollectionModel.of(entities.stream().map((entity) -> model(entity)).collect(Collectors.toList()),
				linkTo(methodOn(getController().getClass()).all(new BooleanBuilder()))
						.withRel(getCollectionRelationName()).withSelfRel());
	}

	public EntityModel<DTO> model(DTO entity) {
		return EntityModel.of(entity, linkTo(methodOn(getController().getClass()).one(entity.getId())).withSelfRel(),
				linkTo(methodOn(getController().getClass()).all(new BooleanBuilder()))
						.withRel(getCollectionRelationName()));
	}

}
