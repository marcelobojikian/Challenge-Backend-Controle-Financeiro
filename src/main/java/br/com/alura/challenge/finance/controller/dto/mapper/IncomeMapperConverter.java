package br.com.alura.challenge.finance.controller.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Income;

public class IncomeMapperConverter implements FinanceMapperConverter<Income, IncomeDTO> {

	private final ModelMapper modelMapper;

	public IncomeMapperConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public void copy(Object source, Object destination) {
		modelMapper.map(source, destination);
	}

	@Override
	public Income toEntity(IncomeDTO dto) {
		return modelMapper.map(dto, Income.class);
	}

	@Override
	public IncomeDTO toDto(Income entity) {
		return modelMapper.map(entity, IncomeDTO.class);
	}

	@Override
	public List<IncomeDTO> toDtoList(Iterable<Income> entities) {
		return modelMapper.map(entities, new TypeToken<List<IncomeDTO>>() {
		}.getType());
	}

}
