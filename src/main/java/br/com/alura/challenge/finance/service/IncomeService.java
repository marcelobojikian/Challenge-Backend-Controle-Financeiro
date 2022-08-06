package br.com.alura.challenge.finance.service;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.repository.IncomeRepository;
import br.com.alura.challenge.finance.service.impl.FinanceDefaultService;

@Service
public class IncomeService extends FinanceDefaultService<Income> {

	public IncomeService(IncomeRepository repository) {
		super(repository);
	}

}
