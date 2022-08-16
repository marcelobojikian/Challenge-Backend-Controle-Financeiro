package br.com.alura.challenge.finance.backend.controller.web.hateoas;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.backend.controller.dto.FinanceDTO;

abstract class SimpleReferenceTest<T extends FinanceDTO> {

	abstract SimpleReference<T> getReference();

	abstract String getResource();

	abstract T getInstance();

	abstract String getCollectionRelationName();

	@Test
	@DisplayName("Should match URI ALL")
	void shouldMatchLinkAll() {

		Predicate predicate = new BooleanBuilder();
		URI expectedUri = templateAll();

		Link link = getReference().linkAll(predicate);
		assertThat(link).isNotNull();

		URI result = link.toUri();
		assertThat(result).isEqualTo(expectedUri);

	}

	@Test
	@DisplayName("Should match URI by Year and Month")
	void shouldMatchLinkYearAndMonth() {

		int year = 2022;
		Month month = Month.AUGUST;
		URI expectedUri = templateYearAndMonth(year, month);

		Link link = getReference().linkByYearAndMonth(year, month);
		assertThat(link).isNotNull();

		URI result = link.toUri();
		assertThat(result).isEqualTo(expectedUri);

	}

	@Test
	@DisplayName("Should match URI ID")
	void shouldMatchLinkId() {

		Long id = 1l;
		URI expectedUri = templateId(id);

		Link link = getReference().linkId(id);
		assertThat(link).isNotNull();

		URI result = link.toUri();
		assertThat(result).isEqualTo(expectedUri);

	}

	@Test
	@DisplayName("Should get Model")
	void shouldGetModel() {

		T entity = getInstance();
		Long idExpected = 1l;

		entity.setId(idExpected);

		EntityModel<T> model = getReference().toModel(entity);

		assertThat(model).isNotNull();
		verifyModel(model, idExpected, new BooleanBuilder());

	}

	@Test
	@DisplayName("Should get List")
	void shouldGetListModel() {

		T firstEntity = getInstance();
		T secondEntity = getInstance();

		List<T> entities = Arrays.asList(firstEntity, secondEntity);

		List<EntityModel<T>> list = getReference().toCollections(entities);

		assertThat(list).isNotNull();
		assertThat(list).hasSize(2);

	}

	@Test
	@DisplayName("Should get CollectionModel")
	void shouldGetCollectionModel() {

		T firstEntity = getInstance();
		T secondEntity = getInstance();

		List<T> entities = Arrays.asList(firstEntity, secondEntity);

		CollectionModel<EntityModel<T>> collectionModel = getReference().toCollectionModel(entities);

		assertThat(collectionModel.getContent()).isNotNull();
		assertThat(collectionModel.hasLink(getCollectionRelationName())).isFalse();

		Optional<Link> selfLink = collectionModel.getLink(IanaLinkRelations.SELF);
		assertThat(selfLink.isPresent()).isTrue();
		Link link = selfLink.get();

		URI uriExpected = templateAll();

		URI result = link.toUri();
		assertThat(result).isEqualTo(uriExpected);

	}

	@Test
	@DisplayName("Should get CollectionModel with Year and Month")
	void shouldGetCollectionModelWithYearAndMonth() {

		int year = 2022;
		Month month = Month.AUGUST;
		T firstEntity = getInstance();
		T secondEntity = getInstance();

		List<T> entities = Arrays.asList(firstEntity, secondEntity);

		CollectionModel<EntityModel<T>> collectionModel = getReference().toCollectionModelByYearAndMonth(entities, year,
				month);
		System.out.println("collectionModel: " + collectionModel);

		assertThat(collectionModel.getContent()).isNotNull();

		Optional<Link> selfLink = collectionModel.getLink(IanaLinkRelations.SELF);
		assertThat(selfLink.isPresent()).isTrue();
		Link link = selfLink.get();

		URI uriExpected = templateYearAndMonth(year, month);

		URI result = link.toUri();
		assertThat(result).isEqualTo(uriExpected);

		Optional<Link> entitiesLink = collectionModel.getLink(getCollectionRelationName());
		assertThat(entitiesLink.isPresent()).isTrue();
		link = entitiesLink.get();

		uriExpected = templateAll();

		result = link.toUri();
		assertThat(result).isEqualTo(uriExpected);

	}

	URI templateId(Long id) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(getResource());
		return builder.path("/{id}").build(id);
	}

	URI templateYearAndMonth(int year, Month month) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(getResource());
		return builder.path("/{year}/{month}").build(year, month);
	}

	URI templateAll() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(getResource());
		UriComponents build = builder.build();
		return build.toUri();
	}

	void verifyModel(EntityModel<T> model, Long id, Predicate predicate) {

		assertThat(model.getContent()).isNotNull();

		Optional<Link> selfLink = model.getLink(IanaLinkRelations.SELF);
		assertThat(selfLink.isPresent()).isTrue();
		Link link = selfLink.get();

		URI uriExpected = templateId(id);

		URI result = link.toUri();
		assertThat(result).isEqualTo(uriExpected);

		Optional<Link> entitiesLink = model.getLink(getCollectionRelationName());
		assertThat(entitiesLink.isPresent()).isTrue();
		link = entitiesLink.get();

		uriExpected = templateAll();

		result = link.toUri();
		assertThat(result).isEqualTo(uriExpected);

	}

}
