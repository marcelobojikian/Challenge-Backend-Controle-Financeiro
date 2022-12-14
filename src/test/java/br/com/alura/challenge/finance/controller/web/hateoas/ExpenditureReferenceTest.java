package br.com.alura.challenge.finance.controller.web.hateoas;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.challenge.finance.controller.dto.ExpenditureDTO;

@ExtendWith(MockitoExtension.class)
class ExpenditureReferenceTest extends SimpleReferenceTest<ExpenditureDTO> {
	
	final String resource = "/api/expenditures";
	
	@InjectMocks
	ExpenditureReference reference;

	@Override
	SimpleReference<ExpenditureDTO> getReference() {
		return reference;
	}

	@Override
	String getResource() {
		return resource;
	}

	@Override
	ExpenditureDTO getInstance() {
		return new ExpenditureDTO();
	}

	@Override
	String getCollectionRelationName() {
		return ExpenditureReference.NAME_COLLECTION_RELATION;
	}

}
