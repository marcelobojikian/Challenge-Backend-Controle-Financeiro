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

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.model.Income;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController implements FinanceController<Income, IncomeDTO> {

	FinanceControllerManager<Income, IncomeDTO> controllerManager;

	public IncomeController(FinanceControllerManager<Income, IncomeDTO> controllerManager) {
		this.controllerManager = controllerManager;
	}

	@Override
	public ResponseEntity<?> all(
			@QuerydslPredicate(root = Income.class, bindings = IncomeDTO.class) Predicate predicate) {

		CollectionModel<EntityModel<IncomeDTO>> collectionModel = controllerManager.findAll(predicate);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(collectionModel);

	}

	@Override
	public ResponseEntity<?> byYearAndMonth(int year, Month month) {

		CollectionModel<EntityModel<IncomeDTO>> collectionModel = controllerManager.allWithYearAndMonth(year, month);

		if (!collectionModel.iterator().hasNext()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(collectionModel);
	}

	@Override
	public ResponseEntity<?> one(Long id) {
		EntityModel<IncomeDTO> entityModel = controllerManager.findById(id);
		return ResponseEntity.ok(entityModel);
	}

	@Override
	public ResponseEntity<?> createFinance(IncomeDTO dto) {
		EntityModel<IncomeDTO> entityModel = controllerManager.create(dto);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

	@Override
	public ResponseEntity<?> updateFinance(Long id, IncomeDTO dto) {
		EntityModel<IncomeDTO> entityModel = controllerManager.update(id, dto);
		return ResponseEntity.ok(entityModel);
	}

	@Override
	public ResponseEntity<?> deleteFinance(Long id) {
		controllerManager.remove(id);
		return ResponseEntity.ok().build();
	}

}
