package br.com.alura.challenge.finance.service;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.repository.ExpenditureRepository;
import br.com.alura.challenge.finance.service.impl.FinanceDefaultService;

@Service
public class ExpenditureService extends FinanceDefaultService<Expenditure>{

	public ExpenditureService(ExpenditureRepository repository) {
		super(repository);
	}

}
