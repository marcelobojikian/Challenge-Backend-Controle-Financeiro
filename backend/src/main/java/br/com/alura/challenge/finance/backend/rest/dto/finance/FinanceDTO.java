package br.com.alura.challenge.finance.backend.rest.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "finances", itemRelation = "finance")
public class FinanceDTO {

	private Long id;

	@NotEmpty
	private String descricao;

	@NotNull
	private BigDecimal valor;

	@NotNull
	private LocalDate data;

	public FinanceDTO() {
	}

	public FinanceDTO(Long id, String descricao, BigDecimal valor, LocalDate data) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
