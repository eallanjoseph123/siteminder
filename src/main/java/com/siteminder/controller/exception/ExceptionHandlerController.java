package com.siteminder.controller.exception;

import com.siteminder.exception.BuilderMessageException;
import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailGatewayResponse;
import com.siteminder.util.EmailServiceStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<EmailGatewayResponse> handleValidationException(EmailException exception) {
		EmailGatewayResponse response = EmailGatewayResponse.builder().message(exception.getMessage()).status(EmailServiceStatus.FAILED.name()).build();

		if (!Objects.isNull(exception.getFromApiErrorMessage()) && exception.getFromApiErrorMessage().length() > 0) {
			response.setFromActualApiMessage(exception.getFromApiErrorMessage());
		}

		return new ResponseEntity<EmailGatewayResponse>(response,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BuilderMessageException.class)
	public ResponseEntity<EmailGatewayResponse> handleValidationException(BuilderMessageException exception) {
		return new ResponseEntity<EmailGatewayResponse>(
				EmailGatewayResponse.builder()
				.message(exception.getMessage())
				.status(EmailServiceStatus.FAILED.name()).build(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<EmailGatewayResponse> validationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();
		List<Map<String,String>> mapList = new LinkedList<>();

		fieldErrors.forEach(field->{
			Map<String,String> map = new HashMap<>();
			map.put(field.getField(),field.getDefaultMessage());
			mapList.add(map);
		});
		return  new ResponseEntity<EmailGatewayResponse>(
				EmailGatewayResponse.builder()
						.fieldsError(mapList)
						.status(EmailServiceStatus.FAILED.name()).build(),
				HttpStatus.NOT_ACCEPTABLE);
	}

}
