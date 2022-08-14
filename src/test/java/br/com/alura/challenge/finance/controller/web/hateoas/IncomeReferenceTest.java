package br.com.alura.challenge.finance.controller.web.hateoas;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;

@ExtendWith(MockitoExtension.class)
class IncomeReferenceTest extends SimpleReferenceTest<IncomeDTO> {
	
	final String resource = "/api/incomes";
	
	@InjectMocks
	IncomeReference reference;

	@Override
	SimpleReference<IncomeDTO> getReference() {
		return reference;
	}

	@Override
	String getResource() {
		return resource;
	}

	@Override
	IncomeDTO getInstance() {
		return new IncomeDTO();
	}

	@Override
	String getCollectionRelationName() {
		return IncomeReference.NAME_COLLECTION_RELATION;
	}

}
