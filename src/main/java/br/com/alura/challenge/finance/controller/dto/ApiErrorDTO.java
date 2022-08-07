package br.com.alura.challenge.finance.controller.dto;

import java.util.List;

public class ApiErrorDTO {

	private String message;
	private List<String> details;

	public ApiErrorDTO(String message, List<String> details) {
		super();
		this.message = message;
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getDetails() {
		return details;
	}

}
