package br.com.alura.challenge.finance.backend.controller.manager;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.model.Expenditure;

@ExtendWith(MockitoExtension.class)
public class ExpenditureControllerManagerTest extends FinanceControllerManagerTest<Expenditure, ExpenditureDTO> {

	@InjectMocks
	FinanceControllerManager<Expenditure, ExpenditureDTO> controller;

	@Override
	public FinanceControllerManager<Expenditure, ExpenditureDTO> getController() {
		return controller;
	}

	@Override
	Expenditure getInstanceEntity() {
		return new Expenditure();
	}

	@Override
	ExpenditureDTO getInstanceDto() {
		return new ExpenditureDTO();
	}

	@Override
	Class<Expenditure> getEntityCLass() {
		return Expenditure.class;
	}

	@Override
	Class<ExpenditureDTO> getDtoCLass() {
		return ExpenditureDTO.class;
	}

}
