package br.com.alura.challenge.finance.controller.error;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseError> handleValidateException(MethodArgumentNotValidException e) {

		BindingResult bindingResult = e.getBindingResult();

		List<String> errors = new ArrayList<String>();

		bindingResult.getFieldErrors()
				.forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));

		String message = errors.size() > 1 ? "Invalid fields" : "Invalid field";

		ResponseError response = new ResponseError(HttpStatus.BAD_REQUEST, message, errors);
		return new ResponseEntity<ResponseError>(response, new HttpHeaders(), response.getStatus());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseError> handleMissingRequestBody(HttpMessageNotReadableException e) {
		Throwable specificCause = e.getMostSpecificCause();

		String message = null;
		String errorMessage = null;

		if (e.contains(DateTimeParseException.class)) {
			message = "Invalid field";
			errorMessage = specificCause.getMessage();
		} else {
			message = "Required request body";
			errorMessage = "body is missing";
		}

		ResponseError response = new ResponseError(HttpStatus.BAD_REQUEST, message, errorMessage);
		return new ResponseEntity<ResponseError>(response, new HttpHeaders(), response.getStatus());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseError> handleBusinessException(BusinessException e) {
		ResponseError apiError = new ResponseError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage());
		return new ResponseEntity<ResponseError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseError> handleNotFoundException(EntityNotFoundException e) {
		ResponseError apiError = new ResponseError(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e.getMessage());
		return new ResponseEntity<ResponseError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}
