package br.com.alura.challenge.finance.backend.rest.dto.exception;

import java.util.Arrays;
import java.util.List;

public class ApiErrorDTO {

	private String message;
	private List<String> details;

	public ApiErrorDTO(final String message, final List<String> details) {
		super();
		this.message = message;
		this.details = details;
	}

    public ApiErrorDTO(final String message, final String detail) {
        super();
        this.message = message;
		this.details = Arrays.asList(detail);
    }

	public String getMessage() {
		return message;
	}

	public List<String> getDetails() {
		return details;
	}

}
