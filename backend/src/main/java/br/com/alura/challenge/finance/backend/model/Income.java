package br.com.alura.challenge.finance.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "receitas")
public class Income extends FinanceEntity {

	private static final long serialVersionUID = 1L;

	public Income() {
		super();
	}

	public Income(Long id, String descricao, BigDecimal valor, LocalDate data) {
		super(id, descricao, valor, data);
	}

}
