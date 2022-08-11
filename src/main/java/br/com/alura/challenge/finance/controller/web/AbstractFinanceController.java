package br.com.alura.challenge.finance.controller.web;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PathVariable;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.model.QExpenditure;
import br.com.alura.challenge.finance.service.FinanceService;

public abstract class AbstractFinanceController<T extends FinanceEntity, DTO extends FinanceDTO>
		implements FinanceController<T, DTO> {

	private FinanceService<T> service;
	private ModelMapper modelMapper;

	private final Class<T> entity;
	private Class<DTO> dto;
	private Type dtoList;

	public AbstractFinanceController(FinanceService<T> service, ModelMapper modelMapper, Class<T> entity,
			Class<DTO> dto) {
		this.service = service;
		this.modelMapper = modelMapper;
		this.entity = entity;
		this.dto = dto;
		this.dtoList = new TypeToken<List<DTO>>() {
		}.getType();
	}

	public CollectionModel<EntityModel<DTO>> findAll(Predicate predicate) {

		Iterable<T> entities = findEntities(predicate);
		List<DTO> finances = covnertDtoList(entities);

		if (finances.isEmpty()) {
			return toCollectionModel(Arrays.asList());
		}

		return toCollectionModel(finances);

	}

	public EntityModel<DTO> findById(Long id) {
		T entity = findEntityById(id);
		DTO entityDTO = covnertDto(entity);
		return toModel(entityDTO);
	}

	public CollectionModel<EntityModel<DTO>> allWithYearAndMonth(int year, Month month) {

		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
		LocalDate lastDayOfMonth = LocalDate.of(year, month, month.maxLength());

		QExpenditure expenditure = QExpenditure.expenditure;
		Predicate predicate = expenditure.data.between(firstDayOfMonth, lastDayOfMonth);

		Iterable<T> entities = findEntities(predicate);
		List<DTO> finances = covnertDtoList(entities);

		if (finances.isEmpty()) {
			return toCollectionModel(Arrays.asList());
		}

		return toCollectionModel(finances);

	}

	public EntityModel<DTO> create(DTO dto) {
		T entity = covnertEntity(dto);
		entity = save(entity);

		DTO entityDTO = covnertDto(entity);
		return toModel(entityDTO);
	}

	public EntityModel<DTO> update(Long id, DTO dto) {
		T entity = updateWithDto(id, dto);

		DTO entityDTO = covnertDto(entity);

		return toModel(entityDTO);
	}

	public void remove(@PathVariable Long id) {
		service.delete(id);
	}

	private T covnertEntity(DTO dto) {
		return modelMapper.map(dto, entity);
	}

	private DTO covnertDto(T entity) {
		return modelMapper.map(entity, dto);
	}

	private List<DTO> covnertDtoList(Iterable<T> entities) {
		return modelMapper.map(entities, dtoList);
	}

	private T findEntityById(Long id) {
		return service.findById(id);
	}

	private Iterable<T> findEntities(Predicate predicate) {
		return service.findAll(predicate);
	}

	private T save(T entity) {
		return service.save(entity);
	}

	private T updateWithDto(Long id, DTO dto) {
		T entity = findEntityById(id);
		modelMapper.map(dto, entity);
		return save(entity);
	}

}
