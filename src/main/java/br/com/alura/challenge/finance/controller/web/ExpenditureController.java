package br.com.alura.challenge.finance.controller.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.QExpenditure;
import br.com.alura.challenge.finance.service.ExpenditureService;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController extends FinanceController<Expenditure, ExpenditureDTO> {

	public static final String NAME_COLLECTION_RELATION = "expenditures";

	public ExpenditureController(ExpenditureService service, ModelMapper modelMapper) {
		super(service, modelMapper, Expenditure.class, ExpenditureDTO.class);
	}

	@GetMapping
	public ResponseEntity<?> findAll(
			@QuerydslPredicate(root = Expenditure.class, bindings = ExpenditureDTO.class) Predicate predicate) {

		Iterable<Expenditure> entities = findEntities(predicate);
		List<ExpenditureDTO> finances = covnertDtoList(entities);

		if (finances.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = toCollectionModel(finances);

		return ResponseEntity.ok(collectionModel);

	}

	@GetMapping("/{year}/{month}")
	public ResponseEntity<?> findByYearAndMonth(@PathVariable int year, @PathVariable Month month) {

		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
		LocalDate lastDayOfMonth = LocalDate.of(year, month, month.maxLength());

		QExpenditure expenditure = QExpenditure.expenditure;
		Predicate predicate = expenditure.data.between(firstDayOfMonth, lastDayOfMonth);

		Iterable<Expenditure> entities = findEntities(predicate);
		List<ExpenditureDTO> finances = covnertDtoList(entities);

		if (finances.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = toCollectionModel(finances);

		return ResponseEntity.ok(collectionModel);

	}

	@GetMapping("/{id}")
	public EntityModel<ExpenditureDTO> findById(@PathVariable Long id) {
		Expenditure entity = findEntityById(id);
		ExpenditureDTO entityDTO = covnertDto(entity);
		return toModel(entityDTO);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid ExpenditureDTO dto) {

		Expenditure entity = covnertEntity(dto);
		entity = save(entity);

		ExpenditureDTO entityDTO = covnertDto(entity);
		EntityModel<ExpenditureDTO> entityModel = toModel(entityDTO);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ExpenditureDTO dto) {

		Expenditure entity = updateWithDto(id, dto);

		ExpenditureDTO entityDTO = covnertDto(entity);

		EntityModel<ExpenditureDTO> entityModel = toModel(entityDTO);

		return ResponseEntity.ok(entityModel);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	Link linkId(Long Id) {
		return linkTo(methodOn(ExpenditureController.class).findById(Id)).withSelfRel();
	}

	@Override
	Link linkAll(Predicate predicate) {
		return linkTo(methodOn(ExpenditureController.class).findAll(new BooleanBuilder()))
				.withRel(NAME_COLLECTION_RELATION);
	}

}
