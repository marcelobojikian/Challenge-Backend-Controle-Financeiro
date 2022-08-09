package br.com.alura.challenge.finance.controller.web;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.PathVariable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.service.FinanceService;

public abstract class FinanceController<T extends FinanceEntity, DTO extends FinanceDTO>
		implements RepresentationModelAssembler<DTO, EntityModel<DTO>> {

	private FinanceService<T> service;
	private ModelMapper modelMapper;

	private Class<T> entity;
	private Class<DTO> dto;
	private Type dtoList;

	public FinanceController(FinanceService<T> service, ModelMapper modelMapper, Class<T> entity, Class<DTO> dto) {
		this.service = service;
		this.modelMapper = modelMapper;
		this.entity = entity;
		this.dto = dto;
		this.dtoList = new TypeToken<List<DTO>>() {
		}.getType();
	}

	public T covnertEntity(DTO dto) {
		return modelMapper.map(dto, entity);
	}

	public DTO covnertDto(T entity) {
		return modelMapper.map(entity, dto);
	}

	public List<DTO> covnertDtoList(Iterable<T> entities) {
		return modelMapper.map(entities, dtoList);
	}

	public T findEntityById(Long id) {
		return service.findById(id);
	}

	public Iterable<T> findEntities(Predicate predicate) {
		return service.findAll(predicate);
	}

	public T save(T entity) {
		return service.save(entity);
	}

	public T updateWithDto(Long id, DTO dto) {
		T entity = findEntityById(id);
		modelMapper.map(dto, entity);
		return save(entity);
	}

	public void remove(@PathVariable Long id) {
		service.delete(id);
	}

	public List<EntityModel<DTO>> toCollections(List<DTO> finances) {
		return finances.stream().map(this::toModel).collect(Collectors.toList());
	}

	@Override
	public EntityModel<DTO> toModel(DTO entity) {
		return EntityModel.of(entity, linkId(entity.getId()), linkAll(new BooleanBuilder()));
	}

	public CollectionModel<EntityModel<DTO>> toCollectionModel(List<DTO> finances) {
		return CollectionModel.of(toCollections(finances), linkAll(new BooleanBuilder()).withSelfRel());
	}

	abstract Link linkId(Long Id);

	abstract Link linkAll(Predicate predicate);

}
