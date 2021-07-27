package com.siteminder.service;

import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.entity.EmailProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component
public class EmailValidator {

	public void validateEmailMessageRequest(EmailMessageRequest emailMessageRequest) {
		if(Objects.isNull(emailMessageRequest)){
			throw new EmailException("Please provide message request details.");
		}

	}

	public void validateEmailProviders(List<EmailProvider> listOfHealthyEmailProvider) {

		if(CollectionUtils.isEmpty(listOfHealthyEmailProvider)){
			throw new EmailException("Email Service is not available at the moment.");
		}
	}
}
