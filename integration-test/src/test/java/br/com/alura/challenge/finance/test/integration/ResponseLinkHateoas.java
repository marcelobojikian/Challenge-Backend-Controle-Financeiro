package br.com.alura.challenge.finance.test.integration;

public interface ResponseLinkHateoas {

	String collectionRelationName();

	default String linkSelfHref() {
		return "_links.self.href";
	}

	default String listPath() {
		return "_embedded." + collectionRelationName();
	}

	default String itemProperty(int index, String property) {
		return listPath() + "[" + index + "]." + property;
	}

	default String linkListHref() {
		return "_links." + collectionRelationName() + ".href";
	}

}
