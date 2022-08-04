package br.com.alura.challenge.finance.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Expenditure implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String descricao;

	@Column
	private BigDecimal valor;
	
	@Column
	private LocalDateTime data;

	public Expenditure(String descricao) {
		this(descricao, BigDecimal.ZERO, LocalDateTime.now());
	}

	public Expenditure(String descricao, BigDecimal valor, LocalDateTime data) {
		this.descricao = descricao;
		this.valor = BigDecimal.ZERO;
		this.data = data;
	}

}
