package br.com.alura.challenge.finance.backend.rest.controller.manager;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PathVariable;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.rest.controller.hateoas.FinanceReference;
import br.com.alura.challenge.finance.backend.rest.controller.manager.mapper.FinanceMapperConverter;
import br.com.alura.challenge.finance.backend.rest.dto.finance.FinanceDTO;
import br.com.alura.challenge.finance.backend.service.FinanceService;

public class FinanceControllerManager<T extends FinanceEntity, DTO extends FinanceDTO> {

	Logger log = LoggerFactory.getLogger(FinanceControllerManager.class);

	private FinanceService<T> service;
	private FinanceMapperConverter<T, DTO> converter;
	private FinanceReference<DTO> ref;

	public FinanceControllerManager(FinanceService<T> service, FinanceMapperConverter<T, DTO> converter,
			FinanceReference<DTO> ref) {
		this.service = service;
		this.converter = converter;
		this.ref = ref;
	}

	public CollectionModel<EntityModel<DTO>> findAll(Predicate predicate) {

		log.debug("Busca por filtro: {}", predicate);
		Iterable<T> entities = service.findAll(predicate);
		List<DTO> finances = converter.toDtoList(entities);

		if (finances.isEmpty()) {
			return ref.toCollectionModel(Arrays.asList());
		}

		return ref.toCollectionModel(finances);

	}

	public EntityModel<DTO> findById(Long id) {
		log.debug("Busca por id: {}", id);
		T entity = service.findById(id);
		DTO entityDTO = converter.toDto(entity);
		return ref.toModel(entityDTO);
	}

	public CollectionModel<EntityModel<DTO>> allWithYearAndMonth(int year, Month month) {

		log.debug("Busca por month: {}/{}", year, month);
		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
		LocalDate lastDayOfMonth = LocalDate.of(year, month, month.maxLength());

		Iterable<T> entities = service.findBetweenDate(firstDayOfMonth, lastDayOfMonth);
		List<DTO> finances = converter.toDtoList(entities);

		if (finances.isEmpty()) {
			return ref.toCollectionModel(Arrays.asList());
		}

		return ref.toCollectionModelByYearAndMonth(finances, year, month);

	}

	public EntityModel<DTO> create(DTO dto) {
		log.debug("Criando finance: {}", dto);
		T entity = converter.toEntity(dto);
		entity = service.save(entity);

		DTO entityDTO = converter.toDto(entity);
		return ref.toModel(entityDTO);
	}

	public EntityModel<DTO> update(Long id, DTO dto) {
		log.debug("Atualizando finance[{}]: {}", id, dto);
		T entity = service.findById(id);
		converter.copy(dto, entity);

		entity = service.save(entity);

		DTO entityDTO = converter.toDto(entity);
		return ref.toModel(entityDTO);
	}

	public void remove(@PathVariable Long id) {
		log.debug("Removendo finance: {}", id);
		service.delete(id);
	}

}
