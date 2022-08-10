package br.com.alura.challenge.finance.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.hateoas.server.core.Relation;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import br.com.alura.challenge.finance.controller.web.ExpenditureController;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.model.QExpenditure;

@Relation(collectionRelation = ExpenditureController.NAME_COLLECTION_RELATION, itemRelation = "expenditure")
public class ExpenditureDTO extends FinanceDTO implements QuerydslBinderCustomizer<QExpenditure> {

	private Categoria categoria;

	public ExpenditureDTO() {
		super();
		this.categoria = Categoria.OUTRAS;
	}

	public ExpenditureDTO(Long id, String descricao, BigDecimal valor, LocalDate data) {
		super(id, descricao, valor, data);
		this.categoria = Categoria.OUTRAS;
	}

	public ExpenditureDTO(Long id, String descricao, BigDecimal valor, LocalDate data, Categoria categoria) {
		super(id, descricao, valor, data);
		this.categoria = categoria;
	}

	@Override
	public void customize(QuerydslBindings bindings, QExpenditure entity) {
		bindings.excluding(entity.id);
		bindings.excluding(entity.data);
		bindings.excluding(entity.valor);
		bindings.excluding(entity.categoria);
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
