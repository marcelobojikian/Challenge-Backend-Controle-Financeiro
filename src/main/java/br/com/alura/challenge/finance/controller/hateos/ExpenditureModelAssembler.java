package br.com.alura.challenge.finance.controller.hateos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.controller.web.ExpenditureController;

@Component
public class ExpenditureModelAssembler implements RepresentationModelAssembler<FinanceDTO, EntityModel<FinanceDTO>> {

	private final static String ENTITY_REL = "expenditures";

	@Override
	public EntityModel<FinanceDTO> toModel(FinanceDTO entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(ExpenditureController.class).findById(entity.getId())).withSelfRel(),
				linkTo(methodOn(ExpenditureController.class).findAll(new BooleanBuilder())).withRel(ENTITY_REL));
	}

	public List<EntityModel<FinanceDTO>> toCollections(List<FinanceDTO> finances) {
		return finances.stream().map(this::toModel).collect(Collectors.toList());
	}

	public CollectionModel<EntityModel<FinanceDTO>> toCollectionModel(List<FinanceDTO> finances) {
		return CollectionModel.of(toCollections(finances),
				linkTo(methodOn(ExpenditureController.class).findAll(new BooleanBuilder())).withSelfRel());
	}

}
