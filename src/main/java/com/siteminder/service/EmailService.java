package com.siteminder.service;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailGatewayResponse;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.model.strategy.EmailProviderBuilderHttpEntityContext;
import com.siteminder.util.EmailErrorMessageConstants;
import com.siteminder.util.EmailServiceStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Service
@AllArgsConstructor
public class EmailService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


	private EmailProviderBuilderHttpEntityContext emailProviderBuilderHttpEntityContext;

	private EmailProviderService emailProviderService;

	@Qualifier("restTemplate")
	private final RestTemplate restTemplate;

	/**
	 * Send or publish the emails to any available or healthy email API providers.
	 * @param emailMessageRequest
	 * @return
	 */
	@Transactional
	public EmailGatewayResponse send(EmailMessageRequest emailMessageRequest) {
		List<EmailProvider> listOfHealthyEmailProvider = this.emailProviderService.getAllHealthyApiProviders();

		validateEmailDetails(emailMessageRequest, listOfHealthyEmailProvider);

		EmailGatewayResponse emailGatewayResponse = new EmailGatewayResponse();

		for (EmailProvider api : listOfHealthyEmailProvider) {
			try {
				HttpEntity<String> request = emailProviderBuilderHttpEntityContext.build(emailMessageRequest, api);
				ResponseEntity<Object> response = restTemplate.exchange(api.getUri().get(), HttpMethod.POST, request, Object.class);

				if (isSuccessfullySent(emailGatewayResponse, api, response)) {
					break;
				}
			} catch (RestClientException e) {
				LOGGER.error(e.getMessage());
				EmailException e1 = new EmailException(EmailErrorMessageConstants.EMAIL_SERVICE_ERROR_MESSAGE);
				e1.setFromApiErrorMessage(e.getMessage());
				api.setStatus(EmailServiceStatus.UNHEALTHY.name());
			} catch (EmailException e) {
				throw e;
			}

			if (EmailServiceStatus.UNHEALTHY.name().equals(api.getStatus())) {
				this.emailProviderService.save(api);
			}
		}

		return emailGatewayResponse;
	}

	private void validateEmailDetails(EmailMessageRequest emailMessageRequest, List<EmailProvider> listOfHealthyEmailProvider) {
		if (Objects.isNull(emailMessageRequest)) {
			throw new EmailException("Please provide message request details.");
		}
		if (CollectionUtils.isEmpty(listOfHealthyEmailProvider)) {
			throw new EmailException("Email Service is not available at the moment.");
		}
	}

	private boolean isSuccessfullySent(EmailGatewayResponse emailGatewayResponse, EmailProvider api, ResponseEntity<Object> response) {

		if (response.getStatusCode().is5xxServerError()) {
			api.setStatus(EmailServiceStatus.UNHEALTHY.name());
			this.emailProviderService.save(api);
			return false;
		}

		if (response.getStatusCode().is2xxSuccessful()) {
			emailGatewayResponse.setStatus(EmailServiceStatus.SUCCESS.name());
			emailGatewayResponse.setMessage("Successfully Sent your Email!");
		}

		return true;
	}
}
