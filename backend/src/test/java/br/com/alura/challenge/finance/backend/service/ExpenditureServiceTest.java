package br.com.alura.challenge.finance.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.repository.ExpenditureRepository;
import br.com.alura.challenge.finance.backend.repository.FinanceRepository;

class ExpenditureParams {

	static Expenditure[] list() {
		return new Expenditure[] { new Expenditure(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")),
				new Expenditure(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022")) };
	}

	static Expenditure one() {
		return new Expenditure(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@ExtendWith(MockitoExtension.class)
class ExpenditureServiceTest extends FinanceServiceTest<Expenditure> {

	@InjectMocks
	ExpenditureService service;

	@Mock
	ExpenditureRepository repository;

	@Override
	FinanceService<Expenditure> getService() {
		return service;
	}

	@Override
	FinanceRepository<Expenditure> getRepository() {
		return repository;
	}

	@Override
	Class<Expenditure> instanceClass() {
		return Expenditure.class;
	}

	@Override
	Expenditure one() {
		return ExpenditureParams.one();
	}

	@Override
	Expenditure[] list() {
		return ExpenditureParams.list();
	}

}
