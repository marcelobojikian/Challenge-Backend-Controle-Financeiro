package br.com.alura.challenge.finance.controller.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;

public class ResponseError {
	
    private final List<String> errors;

    public ResponseError(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ResponseError(EntityNotFoundException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public ResponseError(BusinessException e) {
        this.errors = Arrays.asList(e.getMessage());
    }

    public ResponseError(ResponseStatusException e) {
        this.errors = Arrays.asList(e.getReason());
    }

    public List<String> getErrors() {
        return errors;
    }

}
