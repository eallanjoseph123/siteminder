package com.siteminder.model.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.BuilderMessageException;
import com.siteminder.model.EmailMessageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.util.CollectionUtils;

import java.util.*;

public interface MailProviderHttpEntityBuilder {

	HttpEntity<String> build(EmailMessageRequest emailMessageRequest, EmailProvider api);

	String getName();

	default void populateRequiredEmailDetails(EmailMessageRequest emailMessageRequest, Map<Object, Object> map, Object from) {
		List<Map<Object, Object>> emailToList = getListOfEmailsMap(emailMessageRequest.getEmailTo());

		List<Map<Object, Object>> ccList = getListOfEmailsMap(emailMessageRequest.getEmailCarbonCopies());

		List<Map<Object, Object>> bccList = getListOfEmailsMap(emailMessageRequest.getBlindCarbonCopies());

		map.put("to", emailToList);
		map.put("from", from);
		map.put("cc", ccList);
		map.put("bcc", bccList);
		map.put("subject", emailMessageRequest.getSubject());
	}

	default List<Map<Object, Object>> getListOfEmailsMap(List<String> emailList) {
		if (CollectionUtils.isEmpty(emailList)) {
			return Collections.emptyList();
		}
		List<Map<Object, Object>> result = new LinkedList<>();
		for (String email : emailList) {
			Map<Object, Object> map = new HashMap<>();
			map.put("email", email);
			result.add(map);
		}
		return result;
	}

	default String getBodyAsString(final Object emailMessage) {
		String body = null;
		try {
			body = new ObjectMapper().writeValueAsString(emailMessage);
		} catch (Exception e) {
			throw new BuilderMessageException("http entity build message is broken");
		}
		return body;
	}
}
