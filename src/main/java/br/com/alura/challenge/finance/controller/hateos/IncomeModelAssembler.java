package br.com.alura.challenge.finance.controller.hateos;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.controller.web.IncomeController;

@Component
public class IncomeModelAssembler implements RepresentationModelAssembler<FinanceDTO, EntityModel<FinanceDTO>> {

	private final static String ENTITY_REL = "incomes";

	@Override
	public EntityModel<FinanceDTO> toModel(FinanceDTO entity) {
		return EntityModel.of(entity, linkTo(methodOn(IncomeController.class).findById(entity.getId())).withSelfRel(),
				linkTo(methodOn(IncomeController.class).findAll()).withRel(ENTITY_REL));
	}

	public List<EntityModel<FinanceDTO>> toCollections(List<FinanceDTO> finances) {
		return finances.stream().map(this::toModel).collect(Collectors.toList());
	}

	public CollectionModel<EntityModel<FinanceDTO>> toCollectionModel(List<FinanceDTO> finances) {
		return CollectionModel.of(toCollections(finances),
				linkTo(methodOn(IncomeController.class).findAll()).withSelfRel());
	}

}
