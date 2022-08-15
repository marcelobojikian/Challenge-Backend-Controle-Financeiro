package br.com.alura.challenge.finance.backend.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.exception.BusinessException;
import br.com.alura.challenge.finance.backend.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.backend.model.FinanceEntity;
import br.com.alura.challenge.finance.backend.repository.FinanceRepository;
import br.com.alura.challenge.finance.backend.service.FinanceService;

public abstract class FinanceDefaultService<T extends FinanceEntity> implements FinanceService<T> {

	private FinanceRepository<T> repository;

	public FinanceDefaultService(FinanceRepository<T> repository) {
		this.repository = repository;
	}

	public List<T> findAll() {
		return repository.findAll();
	}

	public Iterable<T> findAll(Predicate predicate) {
		return repository.findAll(predicate);
	}

	public Iterable<T> findBetweenDate(LocalDate startDate, LocalDate endDate) {
		return repository.findAllByDataBetween(startDate, endDate);
	}

	public T findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
	}

	public T save(T entity) {

		String descricao = entity.getDescricao();
		LocalDate firstDayOfMonth = entity.getDataWith(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = entity.getDataWith(TemporalAdjusters.lastDayOfMonth());

		List<T> entities = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(descricao, firstDayOfMonth,
				lastDayOfMonth);

		if (!entities.isEmpty()) {
			throw new BusinessException("There is this finance for this month");
		}

		return repository.save(entity);
	}

	public void delete(Long id) {
		T entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		repository.delete(entity);
	}

}
