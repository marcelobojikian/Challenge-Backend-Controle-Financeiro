package br.com.alura.challenge.finance.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Income;

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

}
