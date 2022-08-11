package br.com.alura.challenge.finance.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.hateoas.server.core.Relation;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import br.com.alura.challenge.finance.controller.web.hateoas.IncomeReference;
import br.com.alura.challenge.finance.model.QIncome;

@Relation(collectionRelation = IncomeReference.NAME_COLLECTION_RELATION, itemRelation = "income")
public class IncomeDTO extends FinanceDTO implements QuerydslBinderCustomizer<QIncome> {

	public IncomeDTO() {
		super();
	}

	public IncomeDTO(Long id, String descricao, BigDecimal valor, LocalDate data) {
		super(id, descricao, valor, data);
	}

	@Override
	public void customize(QuerydslBindings bindings, QIncome entity) {
		bindings.excluding(entity.id);
		bindings.excluding(entity.data);
		bindings.excluding(entity.valor);
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
