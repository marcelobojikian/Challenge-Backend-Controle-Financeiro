package br.com.alura.challenge.finance.backend.service;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.repository.IncomeRepository;
import br.com.alura.challenge.finance.backend.service.impl.FinanceDefaultService;

@Service
public class IncomeService extends FinanceDefaultService<Income> {

	public IncomeService(IncomeRepository repository) {
		super(repository);
	}

}
