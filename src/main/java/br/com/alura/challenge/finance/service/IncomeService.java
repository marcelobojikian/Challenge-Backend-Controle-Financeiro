package br.com.alura.challenge.finance.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@Service
public class IncomeService {

	@Autowired
	private IncomeRepository repository;

	public IncomeService(IncomeRepository repository) {
		this.repository = repository;
	}

	public Income save(Income income) {

		String descricao = income.getDescricao();
		LocalDate firstDayOfMonth = income.getDataWith(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = income.getDataWith(TemporalAdjusters.lastDayOfMonth());

		List<Income> incomes = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(descricao,
				firstDayOfMonth, lastDayOfMonth);

		if (!incomes.isEmpty()) {
			throw new BusinessException("There is this Income for this month");
		}

		return repository.save(income);
	}

	public Income findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
	}

	public Income update(Long id, Income entity) {

		Income entityDB = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));

		boolean sameMonth = entityDB.isSameMonth(entity);

		entityDB.setDescricao(entity.getDescricao());
		entityDB.setValor(entity.getValor());
		entityDB.setData(entity.getData());

		if (sameMonth) {
			return repository.save(entityDB);
		}

		return save(entityDB);

	}

}
