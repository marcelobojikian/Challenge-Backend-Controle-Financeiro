package br.com.alura.challenge.finance.controller.dto.summary;

import java.math.BigDecimal;

import br.com.alura.challenge.finance.model.Expenditure.Categoria;

public class GroupCategory {

	private Categoria category;
	private BigDecimal amount;

	public GroupCategory(Categoria category, BigDecimal amount) {
		this.category = category;
		this.amount = amount;
	}

	public Categoria getCategory() {
		return category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

}
