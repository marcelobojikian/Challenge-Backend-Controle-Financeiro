package br.com.alura.challenge.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "despesas")
public class Expenditure extends FinanceEntity {

	private static final long serialVersionUID = 1L;

	@Column
	@Enumerated(EnumType.STRING)
	private Categoria categoria;

	public enum Categoria {
		ALIMENTACAO, SAUDE, MORADIA, TRANSPORTE, EDUCACAO, LAZER, IMPREVISTOS, OUTRAS;
	}

	public Expenditure() {
		super();
	}

	public Expenditure(Long id, String descricao, BigDecimal valor, LocalDate data) {
		super(id, descricao, valor, data);
		this.categoria = Categoria.OUTRAS;
	}

	public Expenditure(Long id, String descricao, BigDecimal valor, LocalDate data, Categoria categoria) {
		this(id, descricao, valor, data);
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
