package br.com.alura.challenge.finance.backend.controller.dto.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.alura.challenge.finance.backend.controller.dto.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.model.Expenditure;

public class ExpenditureMapperConverter implements FinanceMapperConverter<Expenditure, ExpenditureDTO> {

	private final ModelMapper modelMapper;

	public ExpenditureMapperConverter(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public void copy(Object source, Object destination) {
		modelMapper.map(source, destination);
	}

	@Override
	public Expenditure toEntity(ExpenditureDTO dto) {
		return modelMapper.map(dto, Expenditure.class);
	}

	@Override
	public ExpenditureDTO toDto(Expenditure entity) {
		return modelMapper.map(entity, ExpenditureDTO.class);
	}

	@Override
	public List<ExpenditureDTO> toDtoList(Iterable<Expenditure> entities) {
		return modelMapper.map(entities, new TypeToken<List<ExpenditureDTO>>() {
		}.getType());
	}

}
