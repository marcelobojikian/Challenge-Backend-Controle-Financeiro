package br.com.alura.challenge.finance.controller.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController extends FinanceController<Income, IncomeDTO> {

	public static final String NAME_COLLECTION_RELATION = "incomes";

	public IncomeController(IncomeService service, ModelMapper modelMapper) {
		super(service, modelMapper, Income.class, IncomeDTO.class);
	}

	@GetMapping
	public ResponseEntity<?> findAll(
			@QuerydslPredicate(root = Income.class, bindings = IncomeDTO.class) Predicate predicate) {

		Iterable<Income> entities = findEntities(predicate);
		List<IncomeDTO> finances = covnertDtoList(entities);

		if (finances.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		CollectionModel<EntityModel<IncomeDTO>> collectionModel = toCollectionModel(finances);

		return ResponseEntity.ok(collectionModel);

	}

	@GetMapping("/{id}")
	public EntityModel<IncomeDTO> findById(@PathVariable Long id) {
		Income entity = findEntityById(id);
		IncomeDTO entityDTO = covnertDto(entity);
		return toModel(entityDTO);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid IncomeDTO dto) {

		Income entity = covnertEntity(dto);
		entity = save(entity);

		IncomeDTO entityDTO = covnertDto(entity);
		EntityModel<IncomeDTO> entityModel = toModel(entityDTO);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid IncomeDTO dto) {

		Income entity = updateWithDto(id, dto);

		IncomeDTO entityDTO = covnertDto(entity);
		EntityModel<IncomeDTO> entityModel = toModel(entityDTO);

		return ResponseEntity.ok(entityModel);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		remove(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	Link linkId(Long Id) {
		return linkTo(methodOn(IncomeController.class).findById(Id)).withSelfRel();
	}

	@Override
	Link linkAll(Predicate predicate) {
		return linkTo(methodOn(IncomeController.class).findAll(new BooleanBuilder())).withRel(NAME_COLLECTION_RELATION);
	}

}
