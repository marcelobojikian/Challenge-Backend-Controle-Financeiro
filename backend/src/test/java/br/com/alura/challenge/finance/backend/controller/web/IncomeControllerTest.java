package br.com.alura.challenge.finance.backend.controller.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.rest.controller.IncomeController;
import br.com.alura.challenge.finance.backend.rest.controller.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.backend.rest.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.backend.rest.dto.finance.IncomeDTO;

@Tag("controller")
@ExtendWith(MockitoExtension.class)
class IncomeControllerTest extends FinanceControllerTest<Income, IncomeDTO> {

	@InjectMocks
	IncomeController controller;

	@Mock
	FinanceControllerManager<Income, IncomeDTO> manager;

	@Override
	IncomeDTO getInstanceDto() {
		return new IncomeDTO();
	}

	@Override
	Class<IncomeDTO> getDtoCLass() {
		return IncomeDTO.class;
	}

	@Override
	String getCollectionRelationName() {
		return ExpenditureReference.NAME_COLLECTION_RELATION;
	}

	@Override
	IncomeController getController() {
		return controller;
	}

	@Override
	FinanceControllerManager<Income, IncomeDTO> getManager() {
		return manager;
	}

}
