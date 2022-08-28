package br.com.alura.challenge.finance.backend.rest.dto.summary;

import java.math.BigDecimal;

public class HeaderDTO {

	private BigDecimal income;

	private BigDecimal expenditure;

	private BigDecimal total;

	public HeaderDTO() {
		this(BigDecimal.ZERO, BigDecimal.ZERO);
	}

	public HeaderDTO(BigDecimal income, BigDecimal expenditure) {
		this.income = income;
		this.expenditure = expenditure;
		this.total = income.subtract(expenditure);
	}

	public BigDecimal getIncome() {
		return income;
	}

	public BigDecimal getExpenditure() {
		return expenditure;
	}

	public BigDecimal getTotal() {
		return total;
	}

}
