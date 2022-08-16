package br.com.alura.challenge.finance.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.rest.controller.hateoas.ExpenditureReference;
import br.com.alura.challenge.finance.backend.rest.controller.hateoas.IncomeReference;
import br.com.alura.challenge.finance.backend.rest.controller.manager.FinanceControllerManager;
import br.com.alura.challenge.finance.backend.rest.controller.manager.mapper.ExpenditureMapperConverter;
import br.com.alura.challenge.finance.backend.rest.controller.manager.mapper.IncomeMapperConverter;
import br.com.alura.challenge.finance.backend.rest.dto.finance.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.rest.dto.finance.IncomeDTO;
import br.com.alura.challenge.finance.backend.service.ExpenditureService;
import br.com.alura.challenge.finance.backend.service.IncomeService;

@Configuration
public class ApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		TypeMap<ExpenditureDTO, Expenditure> typeMapExpenditure = modelMapper.createTypeMap(ExpenditureDTO.class,
				Expenditure.class);
		typeMapExpenditure.addMappings(mapper -> mapper.skip(Expenditure::setId));

		TypeMap<IncomeDTO, Income> typeMapIncome = modelMapper.createTypeMap(IncomeDTO.class, Income.class);
		typeMapIncome.addMappings(mapper -> mapper.skip(Income::setId));

		return modelMapper;
	}

	@Bean
	public IncomeMapperConverter incomeModelMapper(ModelMapper modelMapper) {
		return new IncomeMapperConverter(modelMapper);
	}

	@Bean
	public ExpenditureMapperConverter expenditureModelMapper(ModelMapper modelMapper) {
		return new ExpenditureMapperConverter(modelMapper);
	}

	@Bean
	public FinanceControllerManager<Income, IncomeDTO> incomeControllerManager(IncomeService service,
			IncomeMapperConverter converter, IncomeReference ref) {
		return new FinanceControllerManager<>(service, converter, ref);
	}

	@Bean
	public FinanceControllerManager<Expenditure, ExpenditureDTO> expenditureControllerManager(
			ExpenditureService service, ExpenditureMapperConverter converter, ExpenditureReference ref) {
		return new FinanceControllerManager<>(service, converter, ref);
	}

}
