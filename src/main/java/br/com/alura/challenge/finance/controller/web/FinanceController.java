package br.com.alura.challenge.finance.controller.web;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.PathVariable;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.service.FinanceService;

public class FinanceController<T extends FinanceEntity> {

	private FinanceService<T> service;
	private ModelMapper modelMapper;

	public FinanceController(FinanceService<T> service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}

	public List<FinanceDTO> findAll(Predicate predicate) {
		Iterable<T> entities = service.findAll(predicate);
		return modelMapper.map(entities, new TypeToken<List<FinanceDTO>>() {
		}.getType());
	}

	public FinanceDTO findById(Long id) {
		T entity = service.findById(id);
		return modelMapper.map(entity, FinanceDTO.class);
	}

	public FinanceDTO create(T entity) {
		entity = service.save(entity);
		return modelMapper.map(entity, FinanceDTO.class);
	}

	public FinanceDTO update(Long id, T entity) {
		entity = service.update(id, entity);
		return modelMapper.map(entity, FinanceDTO.class);
	}

	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

}
