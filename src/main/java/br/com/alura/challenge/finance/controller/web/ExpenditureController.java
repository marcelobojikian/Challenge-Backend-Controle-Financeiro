package br.com.alura.challenge.finance.controller.web;

import java.time.Month;

import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.controller.dto.mapper.ExpenditureMapperConverter;
import br.com.alura.challenge.finance.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.controller.web.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.service.ExpenditureService;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController implements FinanceController<Expenditure, ExpenditureDTO> {

	FinanceControllerManager<Expenditure, ExpenditureDTO> controllerManager;

	public ExpenditureController(ExpenditureService service, ExpenditureMapperConverter converter,
			ExpenditureReference ref) {
		controllerManager = new FinanceControllerManager<>(service, converter, ref);
	}

	@Override
	public ResponseEntity<?> all(
			@QuerydslPredicate(root = Expenditure.class, bindings = ExpenditureDTO.class) Predicate predicate) {

		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = controllerManager.findAll(predicate);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(collectionModel);

	}

	@Override
	public ResponseEntity<?> byYearAndMonth(int year, Month month) {

		CollectionModel<EntityModel<ExpenditureDTO>> collectionModel = controllerManager.allWithYearAndMonth(year,
				month);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(collectionModel);
	}

	@Override
	public EntityModel<ExpenditureDTO> one(Long id) {
		return controllerManager.findById(id);
	}

	@Override
	public ResponseEntity<?> createFinance(ExpenditureDTO dto) {
		EntityModel<ExpenditureDTO> entityModel = controllerManager.create(dto);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Override
	public ResponseEntity<?> updateFinance(Long id, ExpenditureDTO dto) {
		EntityModel<ExpenditureDTO> entityModel = controllerManager.update(id, dto);
		return ResponseEntity.ok(entityModel);
	}

	@Override
	public ResponseEntity<?> deleteFinance(Long id) {
		controllerManager.remove(id);
		return ResponseEntity.noContent().build();
	}

}
