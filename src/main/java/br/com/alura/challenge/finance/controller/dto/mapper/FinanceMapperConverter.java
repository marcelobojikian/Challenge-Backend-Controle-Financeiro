package br.com.alura.challenge.finance.controller.dto.mapper;

import java.util.List;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.FinanceEntity;

public interface FinanceMapperConverter<T extends FinanceEntity, DTO extends FinanceDTO> {

	void copy(Object source, Object destination);

	public T toEntity(DTO dto);

	public DTO toDto(T entity);

	public List<DTO> toDtoList(Iterable<T> entities);

}
