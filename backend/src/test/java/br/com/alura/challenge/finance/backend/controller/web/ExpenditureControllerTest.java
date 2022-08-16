package br.com.alura.challenge.finance.backend.controller.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.backend.controller.web.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.backend.model.Expenditure;

@Tag("controller")
@ExtendWith(MockitoExtension.class)
class ExpenditureControllerTest extends FinanceControllerTest<Expenditure, ExpenditureDTO> {

	@InjectMocks
	ExpenditureController controller;

	@Mock
	FinanceControllerManager<Expenditure, ExpenditureDTO> manager;

	@Override
	ExpenditureDTO getInstanceDto() {
		return new ExpenditureDTO();
	}

	@Override
	Class<ExpenditureDTO> getDtoCLass() {
		return ExpenditureDTO.class;
	}

	@Override
	String getCollectionRelationName() {
		return ExpenditureReference.NAME_COLLECTION_RELATION;
	}

	@Override
	ExpenditureController getController() {
		return controller;
	}

	@Override
	FinanceControllerManager<Expenditure, ExpenditureDTO> getManager() {
		return manager;
	}

}
