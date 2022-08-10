package br.com.alura.challenge.finance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.alura.challenge.finance.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.controller.dto.summary.HeaderDTO;
import br.com.alura.challenge.finance.repository.ExpenditureRepository;
import br.com.alura.challenge.finance.repository.IncomeRepository;

@Service
public class SummaryService {

	private IncomeRepository incomeRepository;

	private ExpenditureRepository expenditureRepository;

	public SummaryService(ExpenditureRepository expenditureRepository, IncomeRepository incomeRepository) {
		this.expenditureRepository = expenditureRepository;
		this.incomeRepository = incomeRepository;
	}

	public List<GroupCategory> findGroupCategoryByMonth(int year, Month month) {

		YearMonth yearMonth = YearMonth.of(year, month);

		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		return expenditureRepository.findGroupCategoryBetweenDate(firstDayOfMonth, lastDayOfMonth);

	}

	public HeaderDTO findHeaderByMonth(int year, Month month) {

		YearMonth yearMonth = YearMonth.of(year, month);

		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		BigDecimal amountExpenditure = expenditureRepository.getAmountBetweenDate(firstDayOfMonth, lastDayOfMonth);

		if (amountExpenditure == null) {
			amountExpenditure = BigDecimal.ZERO;
		}

		BigDecimal amountIncome = incomeRepository.getAmountBetweenDate(firstDayOfMonth, lastDayOfMonth);

		if (amountIncome == null) {
			amountIncome = BigDecimal.ZERO;
		}

		HeaderDTO header = new HeaderDTO(amountIncome, amountExpenditure);

		return header;
	}

}
