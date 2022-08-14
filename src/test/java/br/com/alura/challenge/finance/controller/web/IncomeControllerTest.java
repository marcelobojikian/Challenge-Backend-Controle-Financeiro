package br.com.alura.challenge.finance.controller.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.controller.web.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.model.Income;

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
