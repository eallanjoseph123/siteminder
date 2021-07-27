package com.siteminder.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailException extends RuntimeException{

	private String fromApiErrorMessage;

	public EmailException(String message) {
		super(message);
	}


}
