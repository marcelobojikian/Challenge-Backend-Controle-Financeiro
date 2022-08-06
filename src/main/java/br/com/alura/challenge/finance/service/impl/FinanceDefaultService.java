package br.com.alura.challenge.finance.service.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.FinanceEntity;
import br.com.alura.challenge.finance.repository.FinanceRepository;
import br.com.alura.challenge.finance.service.FinanceService;

public abstract class FinanceDefaultService<T extends FinanceEntity> implements FinanceService<T> {

	private FinanceRepository<T> repository;

	public FinanceDefaultService(FinanceRepository<T> repository) {
		this.repository = repository;
	}

	public List<T> findAll() {
		return repository.findAll();
	}

	public T findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
	}

	public T save(T entity) {

		String descricao = entity.getDescricao();
		LocalDate firstDayOfMonth = entity.getDataWith(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = entity.getDataWith(TemporalAdjusters.lastDayOfMonth());

		List<T> entities = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(descricao,
				firstDayOfMonth, lastDayOfMonth);

		if (!entities.isEmpty()) {
			throw new BusinessException("There is this finance for this month");
		}

		return repository.save(entity);
	}

	public T update(Long id, T entity) {

		T entityDB = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));

		boolean sameMonth = entityDB.isSameMonth(entity);

		entityDB.setDescricao(entity.getDescricao());
		entityDB.setValor(entity.getValor());
		entityDB.setData(entity.getData());

		if (sameMonth) {
			return repository.save(entityDB);
		}

		return save(entityDB);

	}

	public void delete(Long id) {
		T entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		repository.delete(entity);
	}

}
