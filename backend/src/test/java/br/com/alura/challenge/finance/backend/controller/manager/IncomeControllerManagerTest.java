package br.com.alura.challenge.finance.backend.controller.manager;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.backend.model.Income;

@Tag("controller")
@ExtendWith(MockitoExtension.class)
public class IncomeControllerManagerTest extends FinanceControllerManagerTest<Income, IncomeDTO> {

	@InjectMocks
	FinanceControllerManager<Income, IncomeDTO> controller;

	@Override
	public FinanceControllerManager<Income, IncomeDTO> getController() {
		return controller;
	}

	@Override
	Income getInstanceEntity() {
		return new Income();
	}

	@Override
	IncomeDTO getInstanceDto() {
		return new IncomeDTO();
	}

	@Override
	Class<Income> getEntityCLass() {
		return Income.class;
	}

	@Override
	Class<IncomeDTO> getDtoCLass() {
		return IncomeDTO.class;
	}

}
