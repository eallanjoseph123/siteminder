package com.siteminder.model.strategy;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.BuilderMessageException;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.util.EmailErrorMessageConstants;
import com.siteminder.util.EmailProviderConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component("nylasMessageBuilder")
public class NylasMailProviderHttpEntityBuilder implements MailProviderHttpEntityBuilder {

	private final static Logger LOGGER = Logger.getLogger(NylasMailProviderHttpEntityBuilder.class.getName());


	public HttpEntity<String> build(EmailMessageRequest emailMessageRequest, EmailProvider api) {

		Map<Object, Object> map = new HashMap<>();

		List<Map<Object, Object>> fromList = buildFromMessage(emailMessageRequest);

		populateRequiredEmailDetails(emailMessageRequest, map, fromList);
		map.put("body", emailMessageRequest.getBody());

		return new HttpEntity<>(getBodyAsString(map), getHeaders(api));
	}

	private static HttpHeaders getHeaders(EmailProvider api) {
		if(StringUtils.isEmpty(api.getAccessToken())){
			LOGGER.log(Level.SEVERE, "access token is Required!");
			throw new BuilderMessageException(EmailErrorMessageConstants.EMAIL_SERVICE_ERROR_MESSAGE);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + api.getAccessToken());
		return headers;
	}

	private static List<Map<Object, Object>> buildFromMessage(EmailMessageRequest emailMessageRequest) {
		Map<Object, Object> from = new HashMap<>();
		from.put("email", emailMessageRequest.getEmailFrom());
		List<Map<Object, Object>> fromList = new LinkedList<>();
		fromList.add(from);
		return fromList;
	}

	@Override public String getName() {
		return EmailProviderConstants.NYLAS;
	}
}
