package br.com.alura.challenge.finance.controller.advice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.alura.challenge.finance.controller.dto.ApiErrorDTO;
import br.com.alura.challenge.finance.exception.BusinessException;
import br.com.alura.challenge.finance.exception.EntityNotFoundException;

@RestControllerAdvice
public class RestAdviceController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorDTO> handleValidateException(MethodArgumentNotValidException ex) {

		List<String> details = new ArrayList<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> details.add(error.getField() + ": " + error.getDefaultMessage()));

		ex.getBindingResult().getGlobalErrors().forEach(error -> details.add(error.getDefaultMessage()));

		String messageTittle = details.size() > 1 ? "Invalid fields" : "Invalid field";

		ApiErrorDTO error = new ApiErrorDTO(messageTittle, details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ApiErrorDTO> handleAllExceptions(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ApiErrorDTO error = new ApiErrorDTO("Server Error", details);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiErrorDTO> handleBusinessException(BusinessException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ApiErrorDTO error = new ApiErrorDTO("Error business", details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ApiErrorDTO> handleNotFoundException(EntityNotFoundException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ApiErrorDTO error = new ApiErrorDTO("Record not found", details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		Throwable specificCause = e.getMostSpecificCause();

		if (specificCause instanceof HttpMessageNotReadableException) {
			ApiErrorDTO error = new ApiErrorDTO("Required request body", Arrays.asList("body is missing"));
			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
		} else if (specificCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) specificCause);
		}

		ApiErrorDTO error = new ApiErrorDTO("Invalid field", Arrays.asList(specificCause.getMessage()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ApiErrorDTO> handleInvalidFormatException(InvalidFormatException ex) {
		ApiErrorDTO error = new ApiErrorDTO("Invalid field",
				Arrays.asList("Value '" + ex.getValue() + "' is not valid"));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
