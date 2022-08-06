package br.com.alura.challenge.finance.service;

import java.util.List;

import br.com.alura.challenge.finance.model.FinanceEntity;

public interface FinanceService<T extends FinanceEntity> {
	
	public List<T> findAll();

	public T findById(Long id);

	public T save(T entity);

	public T update(Long id, T entity);

	public void delete(Long id);

}
