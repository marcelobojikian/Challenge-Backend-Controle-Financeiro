package br.com.alura.challenge.finance.controller.web;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.controller.hateos.ExpenditureModelAssembler;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.service.ExpenditureService;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

	private final ExpenditureModelAssembler assembler;

	private final FinanceController<Expenditure> service;

	private final ModelMapper modelMapper;

	public ExpenditureController(ExpenditureService service, ModelMapper modelMapper,
			ExpenditureModelAssembler assembler) {
		this.service = new FinanceController<Expenditure>(service, modelMapper);
		this.modelMapper = modelMapper;
		this.assembler = assembler;
	}

	@GetMapping
	public ResponseEntity<?> findAll(
			@QuerydslPredicate(root = Expenditure.class, bindings = ExpenditureDTO.class) Predicate predicate) {
		List<FinanceDTO> finances = service.findAll(predicate);

		if (finances.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		CollectionModel<EntityModel<FinanceDTO>> collectionModel = assembler.toCollectionModel(finances);

		return ResponseEntity.ok(collectionModel);
	}

	@GetMapping("/{id}")
	public EntityModel<FinanceDTO> findById(@PathVariable Long id) {
		FinanceDTO entity = service.findById(id);
		return assembler.toModel(entity);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid FinanceDTO dto) {
		Expenditure entity = modelMapper.map(dto, Expenditure.class);
		EntityModel<FinanceDTO> entityModel = assembler.toModel(service.create(entity));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid FinanceDTO dto) {
		Expenditure entity = modelMapper.map(dto, Expenditure.class);
		EntityModel<FinanceDTO> entityModel = assembler.toModel(service.update(id, entity));
		return ResponseEntity.ok(entityModel);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
