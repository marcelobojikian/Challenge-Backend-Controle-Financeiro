package br.com.alura.challenge.finance.backend.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.challenge.finance.backend.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.backend.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.backend.controller.dto.mapper.ExpenditureMapperConverter;
import br.com.alura.challenge.finance.backend.controller.dto.mapper.IncomeMapperConverter;
import br.com.alura.challenge.finance.backend.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.model.Income;

@Tag("configuration")
@SpringBootTest(classes = ControleFinanceiroApplication.class)
public class ApplicationBeanTest {

	@Autowired
	ObjectMapper objectMapper; 
	
	@Autowired
	IncomeMapperConverter incomeConverter;

	@Autowired
	ExpenditureMapperConverter expenditureConverter;

	@Autowired
	FinanceControllerManager<Income, IncomeDTO> incomeControllerManager;

	@Autowired
	FinanceControllerManager<Expenditure, ExpenditureDTO> expenditureControllerManager;

	@Test
	void contextLoads() {

		assertThat(objectMapper).isNotNull();
		assertThat(incomeConverter).isNotNull();
		assertThat(expenditureConverter).isNotNull();
		assertThat(incomeControllerManager).isNotNull();
		assertThat(expenditureControllerManager).isNotNull();
		
	}

}
