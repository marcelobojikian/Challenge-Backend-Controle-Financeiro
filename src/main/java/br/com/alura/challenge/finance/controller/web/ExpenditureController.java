package br.com.alura.challenge.finance.controller.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Month;

import org.modelmapper.ModelMapper;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.service.ExpenditureService;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController extends AbstractFinanceController<Expenditure, ExpenditureDTO> {

	public static final String NAME_COLLECTION_RELATION = "expenditures";

	public ExpenditureController(ExpenditureService service, ModelMapper modelMapper) {
		super(service, modelMapper, Expenditure.class, ExpenditureDTO.class);
	}

	@Override
	public ResponseEntity<?> all(
			@QuerydslPredicate(root = Expenditure.class, bindings = ExpenditureDTO.class) Predicate predicate) {
		
		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = findAll(predicate);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(collectionModel);

	}

	@Override
	public ResponseEntity<?> byYearAndMonth(int year, Month month) {
		
		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = allWithYearAndMonth(year, month);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(collectionModel);
	}

	@Override
	public EntityModel<ExpenditureDTO> one(Long id) {
		return findById(id);
	}

	@Override
	public ResponseEntity<?> createFinance(ExpenditureDTO dto) {
		EntityModel<ExpenditureDTO> entityModel = create(dto);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Override
	public ResponseEntity<?> updateFinance(Long id, ExpenditureDTO dto) {
		EntityModel<ExpenditureDTO> entityModel = update(id, dto);
		return ResponseEntity.ok(entityModel);
	}

	@Override
	public ResponseEntity<?> deleteFinance(Long id) {
		remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public Link linkId(Long Id) {
		return linkTo(methodOn(ExpenditureController.class).one(Id)).withSelfRel();
	}

	@Override
	public Link linkAll(Predicate predicate) {
		return linkTo(methodOn(ExpenditureController.class).all(new BooleanBuilder()))
				.withRel(NAME_COLLECTION_RELATION);
	}

}
