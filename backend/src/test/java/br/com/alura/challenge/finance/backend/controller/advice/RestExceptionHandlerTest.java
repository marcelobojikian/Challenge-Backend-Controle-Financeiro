package br.com.alura.challenge.finance.backend.controller.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.alura.challenge.finance.backend.rest.dto.exception.ApiErrorDTO;
import br.com.alura.challenge.finance.backend.rest.exception.BusinessException;
import br.com.alura.challenge.finance.backend.rest.exception.EntityNotFoundException;
import br.com.alura.challenge.finance.backend.rest.handler.RestExceptionHandler;


@Tag("controller")
@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

	@InjectMocks
	RestExceptionHandler handler;

	@Nested
	@DisplayName("Type error 400")
	class Error400 {
		
		@Mock
		TypeMismatchException typeMismatchException;
		
		@Mock
		MissingServletRequestPartException missingServletRequestPartException;
		
		@Mock
		ConstraintViolationException constraintViolationException;
		
		@Mock
		BusinessException businessException;
		
		@Mock
		HttpMessageNotReadableException httpMessageNotReadableException;
		
		@Mock
		InvalidFormatException invalidFormatException;
	
		@Test
		@DisplayName("Should build response with BindException")
		public void shouldBuildResponseWithBindException() {

			BindingResult bindingResultExpected = mock(BindingResult.class);
			BindException bindException = mock(BindException.class, Mockito.withSettings().useConstructor(bindingResultExpected));
			
			String defaultMessageExpected = "Entity error";		
			given(bindException.getLocalizedMessage()).willReturn(defaultMessageExpected);
			
			FieldError fieldErrorExpected = mock(FieldError.class);			
			List mockFieldErrorList = mockList(fieldErrorExpected);
			given(fieldErrorExpected.getField()).willReturn("description");
			given(fieldErrorExpected.getDefaultMessage()).willReturn("is empty");	
			given(bindingResultExpected.getFieldErrors()).willReturn(mockFieldErrorList);
			
			ObjectError objectErrorExpected = mock(ObjectError.class);
			List mockObjectErrorList = mockList(objectErrorExpected);	
			given(objectErrorExpected.getDefaultMessage()).willReturn("Object name must not be null");
			given(bindingResultExpected.getGlobalErrors()).willReturn(mockObjectErrorList);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleBindException(bindException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo(defaultMessageExpected);
			assertThat(result.getDetails()).contains(fieldErrorExpected.getField() + ": " + fieldErrorExpected.getDefaultMessage());
			assertThat(result.getDetails()).contains(objectErrorExpected.getDefaultMessage());
			
		}
	
		@Test
		@DisplayName("Should build response with TypeMismatchException")
		public void shouldBuildResponseWithTypeMismatchException() {
	
			given(typeMismatchException.getLocalizedMessage()).willReturn("Entity error");
			given(typeMismatchException.getValue()).willReturn("description");
			given(typeMismatchException.getPropertyName()).willReturn("teste");
			given(typeMismatchException.getRequiredType()).willReturn(null);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleTypeMismatchException(typeMismatchException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Entity error");
			assertThat(result.getDetails()).contains(typeMismatchException.getValue() + " value for " + typeMismatchException.getPropertyName() + " should be of type "
					+ typeMismatchException.getRequiredType());
			
		}
		
		@Test
		@DisplayName("Should build response with MissingServletRequestPartException")
		public void shouldBuildResponseWithMissingServletRequestPartException() {

			given(missingServletRequestPartException.getLocalizedMessage()).willReturn("Message error");
			given(missingServletRequestPartException.getRequestPartName()).willReturn("that");
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleMissingServletRequestPartException(missingServletRequestPartException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");
			assertThat(result.getDetails()).contains(missingServletRequestPartException.getRequestPartName() + " part is missing");
			
		}
		
		@Test
		@DisplayName("Should build response with MissingServletRequestParameterException")
		public void shouldBuildResponseWithMissingServletRequestParameterException() {
			
			String parameterNameExpectec = "that";

			MissingServletRequestParameterException missingServletRequestParameterException = mock(MissingServletRequestParameterException.class, 
					Mockito.withSettings().useConstructor(
							parameterNameExpectec,
							"Type"));

			given(missingServletRequestParameterException.getLocalizedMessage()).willReturn("Message error");
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleMissingServletRequestParameterException(missingServletRequestParameterException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");
			assertThat(result.getDetails()).contains(missingServletRequestParameterException.getParameterName() + " parameter is missing");
			
		}
		
		@Test
		@DisplayName("Should build response with BusinessException")
		public void shouldBuildResponseWithBusinessException() {

			given(businessException.getLocalizedMessage()).willReturn("Message error");
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleBusinessException(businessException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Error business");
			assertThat(result.getDetails()).contains(businessException.getLocalizedMessage());
			
		}
		
		@Test
		@DisplayName("Should build response with ConstraintViolationException")
		public void shouldBuildResponseWithConstraintViolationException() {
			
			ConstraintViolation violationExpected = mock(ConstraintViolation.class);
			Path pathExpected = mock(Path.class);
			
			given(violationExpected.getRootBeanClass()).willReturn(violationExpected.getClass());
			given(violationExpected.getPropertyPath()).willReturn(pathExpected);
			given(pathExpected.toString()).willReturn("path");
			given(violationExpected.getMessage()).willReturn("message");
			
			Set mockSet = mockSet(violationExpected);					
			given(constraintViolationException.getLocalizedMessage()).willReturn("Message error");		
			given(constraintViolationException.getConstraintViolations()).willReturn(mockSet);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleConstraintViolationException(constraintViolationException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");			
			assertThat(result.getDetails()).contains(violationExpected.getRootBeanClass().getName() + " " + violationExpected.getPropertyPath() + ": " + violationExpected.getMessage());
			
		}
		
		@Test
		@DisplayName("Should build response with HttpMessageNotReadableException")
		public void shouldBuildResponseWithHttpMessageNotReadableException() {
			
			given(httpMessageNotReadableException.getMostSpecificCause()).willReturn(httpMessageNotReadableException);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleHttpMessageNotReadableException(httpMessageNotReadableException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Required request body");
			assertThat(result.getDetails()).contains("body is missing");
			
		}
		
		@Test
		@DisplayName("Should build response with Not Mapped HttpMessageNotReadableException")
		public void shouldBuildResponseWithNotMappedHttpMessageNotReadableException() {
			
			given(httpMessageNotReadableException.getMostSpecificCause()).willReturn(new RuntimeException("not mapped"));

			assertThrows(Exception.class, () -> {
				handler.handleHttpMessageNotReadableException(httpMessageNotReadableException);
			});
			
		}
		
		@Test
		@DisplayName("Should build response with InvalidFormatException")
		public void shouldBuildResponseWithInvalidFormatException() {
			
			given(httpMessageNotReadableException.getMostSpecificCause()).willReturn(invalidFormatException);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleHttpMessageNotReadableException(httpMessageNotReadableException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Invalid field");
			assertThat(result.getDetails()).contains("Value '" + ((InvalidFormatException) invalidFormatException).getValue() + "' is not valid");
			
		}
		
	}

	@Nested
	@DisplayName("Type error 404")
	class Error404 {
		
		@Mock
		NoHandlerFoundException noHandlerFoundException;
		
		@Mock
		EntityNotFoundException entityNotFoundException;
		
		@Test
		@DisplayName("Should build response with NoHandlerFoundException")
		public void shouldBuildResponseWithNoHandlerFoundException() {
			
			given(noHandlerFoundException.getLocalizedMessage()).willReturn("Message error");	
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleNoHandlerFoundException(noHandlerFoundException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");
			assertThat(result.getDetails()).contains("No handler found for " + noHandlerFoundException.getHttpMethod() + " " + noHandlerFoundException.getRequestURL());
			
		}
		
		@Test
		@DisplayName("Should build response with EntityNotFoundException")
		public void shouldBuildResponseWithEntityNotFoundException() {
			
			given(entityNotFoundException.getLocalizedMessage()).willReturn("Finance not found");
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleEntityNotFoundException(entityNotFoundException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Record not found");
			assertThat(result.getDetails()).contains("Finance not found");
			
		}
		
	}

	@Nested
	@DisplayName("Type error 405")
	class Error405 {
		
		@Mock
		HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException;
		
		@Test
		@DisplayName("Should build response with HttpRequestMethodNotSupportedException")
		public void shouldBuildResponseWithHttpRequestMethodNotSupportedException() {
			
			Set mockSet = mockSet(HttpMethod.GET);
			
			given(httpRequestMethodNotSupportedException.getLocalizedMessage()).willReturn("Message error");	
			given(httpRequestMethodNotSupportedException.getMethod()).willReturn("/test");	
			given(httpRequestMethodNotSupportedException.getSupportedHttpMethods()).willReturn(mockSet);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleHttpRequestMethodNotSupportedException(httpRequestMethodNotSupportedException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");
			assertThat(result.getDetails()).contains("/test method is not supported for this request. Supported methods are GET ");
			
		}
		
	}

	@Nested
	@DisplayName("Type error 415")
	class Error415 {
		
		@Mock
		HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException;
		
		@Test
		@DisplayName("Should build response with HttpMediaTypeNotSupportedException")
		public void shouldBuildResponseWithHttpMediaTypeNotSupportedException() {
			
			List mockList = mockList(MediaType.APPLICATION_JSON);
			
			given(httpMediaTypeNotSupportedException.getLocalizedMessage()).willReturn("Message error");	
			given(httpMediaTypeNotSupportedException.getContentType()).willReturn(MediaType.TEXT_PLAIN);	
			given(httpMediaTypeNotSupportedException.getSupportedMediaTypes()).willReturn(mockList);
			
			ResponseEntity<ApiErrorDTO> handleValidateException = handler.handleHttpMediaTypeNotSupportedException(httpMediaTypeNotSupportedException);
			ApiErrorDTO result = handleValidateException.getBody();
			
			assertThat(handleValidateException.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			assertThat(result).isNotNull();
			
			assertThat(result.getMessage()).isEqualTo("Message error");
			assertThat(result.getDetails()).contains(httpMediaTypeNotSupportedException.getContentType()+" media type is not supported. Supported media types are " + MediaType.APPLICATION_JSON_VALUE);
			
		}
		
	}
	
	public List mockList(Object objReturn) {
		
		Iterator mockIterator = mock(Iterator.class);
		List supportedMediaTypes = mock(List.class);
		
		doCallRealMethod().when(supportedMediaTypes).forEach(Mockito.any(Consumer.class));
		Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
		Mockito.when(mockIterator.next()).thenReturn(objReturn);
		Mockito.when(supportedMediaTypes.iterator()).thenReturn(mockIterator);
		
		return supportedMediaTypes;
		
	}
	
	public Set mockSet(Object objReturn) {
		
		Iterator mockIterator = mock(Iterator.class);
		Set supportedMediaTypes = mock(Set.class);
		
		doCallRealMethod().when(supportedMediaTypes).forEach(Mockito.any(Consumer.class));
		Mockito.when(mockIterator.hasNext()).thenReturn(true, false);
		Mockito.when(mockIterator.next()).thenReturn(objReturn);
		Mockito.when(supportedMediaTypes.iterator()).thenReturn(mockIterator);
		
		return supportedMediaTypes;
		
	}

}
