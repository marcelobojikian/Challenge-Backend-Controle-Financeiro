package br.com.alura.challenge.finance.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.backend.model.Income;
import br.com.alura.challenge.finance.backend.repository.FinanceRepository;
import br.com.alura.challenge.finance.backend.repository.IncomeRepository;

class IncomeParams {

	static Income[] list() {
		return new Income[] { new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022")),
				new Income(2l, "Test 2", BigDecimal.valueOf(44), toDate("11/07/2022")) };
	}

	static Income one() {
		return new Income(1l, "Test", BigDecimal.valueOf(23), toDate("03/08/2022"));
	}

	static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}

@Tag("service")
@ExtendWith(MockitoExtension.class)
class IncomeServiceTest extends FinanceServiceTest<Income> {

	@InjectMocks
	IncomeService service;

	@Mock
	IncomeRepository repository;

	@Override
	FinanceService<Income> getService() {
		return service;
	}

	@Override
	FinanceRepository<Income> getRepository() {
		return repository;
	}

	@Override
	Class<Income> instanceClass() {
		return Income.class;
	}

	@Override
	Income one() {
		return IncomeParams.one();
	}

	@Override
	Income[] list() {
		return IncomeParams.list();
	}

}
