package br.com.alura.challenge.finance.controller.dto;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import br.com.alura.challenge.finance.model.QExpenditure;

public class ExpenditureDTO extends FinanceDTO implements QuerydslBinderCustomizer<QExpenditure> {

	@Override
	public void customize(QuerydslBindings bindings, QExpenditure entity) {
		bindings.excluding(entity.id);
		bindings.excluding(entity.data);
		bindings.excluding(entity.valor);
		bindings.bind(String.class)
				.first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
	}

}
