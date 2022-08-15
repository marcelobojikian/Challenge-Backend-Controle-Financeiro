package br.com.alura.challenge.finance.backend.controller.web.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Month;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.controller.web.ExpenditureController;

@Service
public class ExpenditureReference implements SimpleReference<ExpenditureDTO> {

	public static final String NAME_COLLECTION_RELATION = "expenditures";

	@Override
	public Link linkId(Long Id) {
		return linkTo(methodOn(ExpenditureController.class).one(Id)).withSelfRel();
	}

	@Override
	public Link linkAll(Predicate predicate) {
		return linkTo(methodOn(ExpenditureController.class).all(predicate)).withRel(NAME_COLLECTION_RELATION);
	}

	@Override
	public Link linkByYearAndMonth(int year, Month month) {
		return linkTo(methodOn(ExpenditureController.class).byYearAndMonth(year, month)).withRel(NAME_COLLECTION_RELATION);
	}

}
