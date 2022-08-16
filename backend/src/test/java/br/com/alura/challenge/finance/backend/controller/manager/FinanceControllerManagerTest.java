package br.com.alura.challenge.finance.backend.controller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.backend.controller.dto.mapper.FinanceMapperConverter;
import br.com.alura.challenge.finance.backend.controller.web.hateoas.SimpleReference;
import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.service.FinanceService;

abstract class FinanceControllerManagerTest<T extends FinanceEntity, DTO extends FinanceDTO> {

	abstract FinanceControllerManager<T, DTO> getController();

	abstract T getInstanceEntity();

	abstract DTO getInstanceDto();

	abstract Class<T> getEntityCLass();

	abstract Class<DTO> getDtoCLass();

	@Mock
	FinanceService<T> service;
	@Mock
	FinanceMapperConverter<T, DTO> converter;
	@Mock
	SimpleReference<DTO> ref;

	@Nested
	@DisplayName("Find By Id")
	class FindById {

		@Test
		void shouldFindOneOk() {

			T entity = getInstanceEntity();
			DTO entityDTO = getInstanceDto();
			EntityModel<DTO> model = EntityModel.of(entityDTO);

			given(service.findById(anyLong())).willReturn(entity);
			given(converter.toDto(any(getEntityCLass()))).willReturn(entityDTO);
			given(ref.toModel(entityDTO)).willReturn(model);

			EntityModel<DTO> result = getController().findById(1l);

			assertThat(result).isEqualTo(model);

		}

	}

	@Nested
	@DisplayName("Find All")
	class FindAll {

		@Test
		void shouldFindAllOk() {

			List<T> entities = Arrays.asList(getInstanceEntity());
			List<DTO> entityListDTO = Arrays.asList(getInstanceDto());
			List<EntityModel<DTO>> listModel = entityListDTO.stream().map((dto) -> EntityModel.of(dto))
					.collect(Collectors.toList());
			CollectionModel<EntityModel<DTO>> collectiomModel = CollectionModel.of(listModel);

			given(service.findAll(any(Predicate.class))).willReturn(entities);
			given(converter.toDtoList(entities)).willReturn(entityListDTO);
			given(ref.toCollectionModel(entityListDTO)).willReturn(collectiomModel);

			CollectionModel<EntityModel<DTO>> result = getController().findAll(new BooleanBuilder());

			assertThat(result).isEqualTo(collectiomModel);

		}

		@Test
		void shouldFindAllWithNoContent() {

			List<T> entities = Arrays.asList();
			List<DTO> entityListDTO = Arrays.asList();
			CollectionModel<EntityModel<DTO>> collectiomModel = CollectionModel.empty();

			given(service.findAll(any(Predicate.class))).willReturn(Arrays.asList());
			given(converter.toDtoList(entities)).willReturn(Arrays.asList());
			given(ref.toCollectionModel(entityListDTO)).willReturn(collectiomModel);

			CollectionModel<EntityModel<DTO>> result = getController().findAll(new BooleanBuilder());

			assertThat(result).isEqualTo(collectiomModel);

		}

	}

	@Nested
	@DisplayName("Find with Year and MonthMONTH")
	class FindAllWithYearAndMonth {

		@Test
		void shouldGetByYearAndMonthOk() {

			List<T> entities = Arrays.asList(getInstanceEntity());
			List<DTO> entityListDTO = Arrays.asList(getInstanceDto());
			List<EntityModel<DTO>> listModel = entityListDTO.stream().map((dto) -> EntityModel.of(dto))
					.collect(Collectors.toList());
			CollectionModel<EntityModel<DTO>> collectiomModel = CollectionModel.of(listModel);

			given(service.findBetweenDate(any(LocalDate.class), any(LocalDate.class))).willReturn(entities);
			given(converter.toDtoList(entities)).willReturn(entityListDTO);
			given(ref.toCollectionModelByYearAndMonth(any(List.class), any(Integer.class), any(Month.class)))
					.willReturn(collectiomModel);

			CollectionModel<EntityModel<DTO>> result = getController().allWithYearAndMonth(2022, Month.AUGUST);

			assertThat(result).isEqualTo(collectiomModel);

		}

		@Test
		void shouldGetByYearAndMonthNoContent() {

			List<T> entities = Arrays.asList();
			List<DTO> entityListDTO = Arrays.asList();
			List<EntityModel<DTO>> listModel = entityListDTO.stream().map((dto) -> EntityModel.of(dto))
					.collect(Collectors.toList());
			CollectionModel<EntityModel<DTO>> collectiomModel = CollectionModel.of(listModel);

			given(service.findBetweenDate(any(LocalDate.class), any(LocalDate.class))).willReturn(entities);
			given(converter.toDtoList(entities)).willReturn(entityListDTO);
			given(ref.toCollectionModel(entityListDTO)).willReturn(collectiomModel);

			CollectionModel<EntityModel<DTO>> result = getController().allWithYearAndMonth(2022, Month.AUGUST);

			assertThat(result).isEqualTo(collectiomModel);

		}

	}

	@Nested
	@DisplayName("Create")
	class Create {

		@Test
		void shouldCreated() {

			T entity = getInstanceEntity();
			DTO entityDTO = getInstanceDto();
			EntityModel<DTO> model = EntityModel.of(entityDTO);

			given(converter.toEntity(any(getDtoCLass()))).willReturn(entity);
			given(service.save(any(getEntityCLass()))).willReturn(entity);
			given(converter.toDto(any(getEntityCLass()))).willReturn(entityDTO);
			given(ref.toModel(any(getDtoCLass()))).willReturn(model);

			EntityModel<DTO> result = getController().create(entityDTO);

			assertThat(result).isEqualTo(model);

		}

	}

	@Nested
	@DisplayName("Update")
	class Update {

		@Test
		void shouldUpdate() {

			Long id = 1l;
			T entity = getInstanceEntity();
			DTO entityDTO = getInstanceDto();
			EntityModel<DTO> model = EntityModel.of(entityDTO);

			given(service.findById(any(Long.class))).willReturn(entity);
			given(service.save(any(getEntityCLass()))).willReturn(entity);
			given(converter.toDto(any(getEntityCLass()))).willReturn(entityDTO);
			given(ref.toModel(any(getDtoCLass()))).willReturn(model);

			EntityModel<DTO> result = getController().update(id, entityDTO);

			assertThat(result).isEqualTo(model);

		}

	}

	@Nested
	@DisplayName("Remove")
	class Remove {

		@Test
		void shouldDelete() {

			Long id = 1l;
			getController().remove(id);

		}

	}

}
