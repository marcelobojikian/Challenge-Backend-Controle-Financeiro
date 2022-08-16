package br.com.alura.challenge.finance.backend.rest.dto.summary;

import java.util.List;

public class SummaryDTO {

	private HeaderDTO balance;

	private List<GroupCategory> categories;

	public SummaryDTO(HeaderDTO header, List<GroupCategory> categories) {
		super();
		this.balance = header;
		this.categories = categories;
	}

	public HeaderDTO getBalance() {
		return balance;
	}

	public List<GroupCategory> getCategories() {
		return categories;
	}

}
