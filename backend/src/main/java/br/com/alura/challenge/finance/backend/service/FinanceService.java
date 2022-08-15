package br.com.alura.challenge.finance.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;

public interface FinanceService<T extends FinanceEntity> {

	public List<T> findAll();

	public Iterable<T> findAll(Predicate predicate);

	public Iterable<T> findBetweenDate(LocalDate startDate, LocalDate endDate);

	public T findById(Long id);

	public T save(T entity);

	public void delete(Long id);

}
