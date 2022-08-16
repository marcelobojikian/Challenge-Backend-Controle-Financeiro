package br.com.alura.challenge.finance.backend.rest.controller.manager.mapper;

import java.util.List;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.rest.dto.finance.FinanceDTO;

public interface FinanceMapperConverter<T extends FinanceEntity, DTO extends FinanceDTO> {

	void copy(Object source, Object destination);

	public T toEntity(DTO dto);

	public DTO toDto(T entity);

	public List<DTO> toDtoList(Iterable<T> entities);

}
