package br.com.alura.challenge.finance.backend.rest.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.alura.challenge.finance.backend.rest.dto.exception.ApiErrorDTO;
import br.com.alura.challenge.finance.backend.rest.exception.BusinessException;
import br.com.alura.challenge.finance.backend.rest.exception.EntityNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

	Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

	// 400

	@ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
	public final ResponseEntity<ApiErrorDTO> handleBindException(final BindException ex) {
		log.info(ex.getClass().getName());

		List<String> details = new ArrayList<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> details.add(error.getField() + ": " + error.getDefaultMessage()));

		ex.getBindingResult().getGlobalErrors().forEach(error -> details.add(error.getDefaultMessage()));

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({TypeMismatchException.class, MethodArgumentTypeMismatchException.class})
	public final ResponseEntity<ApiErrorDTO> handleTypeMismatchException(final TypeMismatchException ex) {
		log.info(ex.getClass().getName());

		final String detail = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), detail);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public final ResponseEntity<ApiErrorDTO> handleMissingServletRequestPartException(
			final MissingServletRequestPartException ex) {
		log.info(ex.getClass().getName());

		final String detail = ex.getRequestPartName() + " part is missing";

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), detail);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public final ResponseEntity<ApiErrorDTO> handleMissingServletRequestParameterException(
			final MissingServletRequestParameterException ex) {
		log.info(ex.getClass().getName());

		final String detail = ex.getParameterName() + " parameter is missing";

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), detail);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessException.class)
	public final ResponseEntity<ApiErrorDTO> handleBusinessException(final BusinessException ex) {
		log.info(ex.getClass().getName());

		final String message = "Error business";
		final String detail = ex.getLocalizedMessage();

		final ApiErrorDTO error = new ApiErrorDTO(message, detail);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ApiErrorDTO> handleConstraintViolationException(final ConstraintViolationException ex) {
		log.info(ex.getClass().getName());

		final List<String> details = new ArrayList<String>();
		ex.getConstraintViolations().forEach(error -> details
				.add(error.getRootBeanClass().getName() + " " + error.getPropertyPath() + ": " + error.getMessage()));

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public final ResponseEntity<ApiErrorDTO> handleHttpMessageNotReadableException(
			final HttpMessageNotReadableException ex) {
		log.info(ex.getClass().getName());

		Throwable specificCause = ex.getMostSpecificCause();
		ApiErrorDTO error = null;

		if (specificCause instanceof HttpMessageNotReadableException) {
			final String message = "Required request body";
			final String detail = "body is missing";
			error = new ApiErrorDTO(message, detail);
		} else if (specificCause instanceof InvalidFormatException) {
			final String message = "Invalid field";
			final String detail = "Value '" + ((InvalidFormatException) specificCause).getValue() + "' is not valid";
			error = new ApiErrorDTO(message, detail);
		} else {
			throw new RuntimeException("Http message error not found", ex);
		}

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 404

	@ExceptionHandler(NoHandlerFoundException.class)
	public final ResponseEntity<ApiErrorDTO> handleNoHandlerFoundException(final NoHandlerFoundException ex) {
		log.info(ex.getClass().getName());

		final String detail = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), detail);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<ApiErrorDTO> handleEntityNotFoundException(final EntityNotFoundException ex) {
		log.info(ex.getClass().getName());

		final String message = "Record not found";
		final String detail = ex.getLocalizedMessage();

		final ApiErrorDTO error = new ApiErrorDTO(message, detail);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// 405

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public final ResponseEntity<ApiErrorDTO> handleHttpRequestMethodNotSupportedException(
			final HttpRequestMethodNotSupportedException ex) {
		log.info(ex.getClass().getName());

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");

		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), builder.toString());
		return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}

	// 415

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public final ResponseEntity<ApiErrorDTO> handleHttpMediaTypeNotSupportedException(
			final HttpMediaTypeNotSupportedException ex) {
		log.info(ex.getClass().getName());

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");

		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		final ApiErrorDTO error = new ApiErrorDTO(ex.getLocalizedMessage(), builder.substring(0, builder.length() - 1));
		return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

}
