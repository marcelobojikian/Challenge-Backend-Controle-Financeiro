package br.com.alura.challenge.finance.backend.rest.controller.hateoas;

import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.rest.dto.finance.FinanceDTO;

public interface FinanceReference<T extends FinanceDTO> extends RepresentationModelAssembler<T, EntityModel<T>> {

	Link linkId(Long Id);

	Link linkAll(Predicate predicate);

	Link linkByYearAndMonth(int year, Month month);

	default EntityModel<T> toModel(T entity) {
		return EntityModel.of(entity, linkId(entity.getId()), linkAll(new BooleanBuilder()));
	}

	default List<EntityModel<T>> toCollections(List<T> entities) {
		return entities.stream().map(this::toModel).collect(Collectors.toList());
	}

	default CollectionModel<EntityModel<T>> toCollectionModel(List<T> entities) {
		return CollectionModel.of(toCollections(entities), linkAll(new BooleanBuilder()).withSelfRel());
	}

	default CollectionModel<EntityModel<T>> toCollectionModelByYearAndMonth(List<T> entities, int year, Month month) {
		return CollectionModel.of(toCollections(entities), linkAll(new BooleanBuilder()),
				linkByYearAndMonth(year, month).withSelfRel());
	}

}
