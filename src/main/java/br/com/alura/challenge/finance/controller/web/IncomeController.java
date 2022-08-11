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

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController extends AbstractFinanceController<Income, IncomeDTO> {

	public static final String NAME_COLLECTION_RELATION = "incomes";

	public IncomeController(IncomeService service, ModelMapper modelMapper) {
		super(service, modelMapper, Income.class, IncomeDTO.class);
	}

	@Override
	public ResponseEntity<?> all(
			@QuerydslPredicate(root = Income.class, bindings = IncomeDTO.class) Predicate predicate) {
		
		CollectionModel<EntityModel<IncomeDTO>> collectionModel = findAll(predicate);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(collectionModel);

	}

	@Override
	public ResponseEntity<?> byYearAndMonth(int year, Month month) {
		
		CollectionModel<EntityModel<IncomeDTO>> collectionModel = allWithYearAndMonth(year, month);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(collectionModel);
	}

	@Override
	public EntityModel<IncomeDTO> one(Long id) {
		return findById(id);
	}

	@Override
	public ResponseEntity<?> createFinance(IncomeDTO dto) {
		EntityModel<IncomeDTO> entityModel = create(dto);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Override
	public ResponseEntity<?> updateFinance(Long id, IncomeDTO dto) {
		EntityModel<IncomeDTO> entityModel = update(id, dto);
		return ResponseEntity.ok(entityModel);
	}

	@Override
	public ResponseEntity<?> deleteFinance(Long id) {
		remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public Link linkId(Long Id) {
		return linkTo(methodOn(IncomeController.class).one(Id)).withSelfRel();
	}

	@Override
	public Link linkAll(Predicate predicate) {
		return linkTo(methodOn(IncomeController.class).all(new BooleanBuilder())).withRel(NAME_COLLECTION_RELATION);
	}

}
