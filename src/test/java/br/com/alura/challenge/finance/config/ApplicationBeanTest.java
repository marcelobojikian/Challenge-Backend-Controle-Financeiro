package br.com.alura.challenge.finance.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.challenge.finance.ControleFinanceiroApplication;
import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.controller.dto.mapper.ExpenditureMapperConverter;
import br.com.alura.challenge.finance.controller.dto.mapper.IncomeMapperConverter;
import br.com.alura.challenge.finance.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Income;

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
