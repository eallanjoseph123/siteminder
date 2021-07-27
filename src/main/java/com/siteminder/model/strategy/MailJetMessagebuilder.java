package com.siteminder.model.strategy;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.BuilderMessageException;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.util.EmailErrorMessageConstants;
import com.siteminder.util.EmailProviderConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Component("mailJetMessagebuilder")
public class MailJetMessagebuilder implements MailProviderHttpEntityBuilder {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


	public HttpEntity<String> build(EmailMessageRequest emailMessageRequest, EmailProvider api) {

		Map<Object, List<Object>> messages = new HashMap<>();

		Map<Object, Object> map = new HashMap<>();

		Map<Object, Object> fromMap = buildFromMessage(emailMessageRequest.getEmailFrom());

		populateRequiredEmailDetails(emailMessageRequest, map, fromMap);

		map.put("TextPart", emailMessageRequest.getBody());
		List<Object> listMessage = new LinkedList<>();
		listMessage.add(map);
		messages.put("Messages", listMessage);

		return new HttpEntity<>(getBodyAsString(messages), getHeaders(api));
	}

	private HttpHeaders getHeaders(EmailProvider api) {
		String basicAuth = api.getBased64Creds();
		if(StringUtils.isEmpty(basicAuth)){
			LOGGER.error( "access token is Required!");
			throw new BuilderMessageException(EmailErrorMessageConstants.EMAIL_SERVICE_ERROR_MESSAGE);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + basicAuth);
		return headers;
	}

	private Map<Object, Object> buildFromMessage(String emailFrom) {
		Map<Object, Object> from = new HashMap<>();
		from.put("email", emailFrom);
		return from;
	}

	@Override
	public String getName() {
		return EmailProviderConstants.MAILJET;
	}
}
