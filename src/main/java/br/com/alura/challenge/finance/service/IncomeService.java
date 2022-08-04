package br.com.alura.challenge.finance.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@Service
public class IncomeService {

	private IncomeRepository repository;

	public IncomeService(IncomeRepository repository) {
		this.repository = repository;
	}

	public Income create(Income income) {

		LocalDate dateIncome = income.getData();
		String descricao = income.getDescricao().trim();

		LocalDate firstDayOfMonth = dateIncome.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate lastDayOfMonth = dateIncome.with(TemporalAdjusters.lastDayOfMonth());

		List<Income> incomes = repository.findAllByDescricaoContainingIgnoreCaseAndDataBetween(descricao,
				firstDayOfMonth, lastDayOfMonth);

		if (!incomes.isEmpty()) {
			throw new BusinessException("Income already created for this month");
		}

		return repository.save(income);
	}

}
