package br.com.alura.challenge.finance.backend.service;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.repository.ExpenditureRepository;
import br.com.alura.challenge.finance.backend.service.impl.FinanceDefaultService;

@Service
public class ExpenditureService extends FinanceDefaultService<Expenditure> {

	public ExpenditureService(ExpenditureRepository repository) {
		super(repository);
	}

}
