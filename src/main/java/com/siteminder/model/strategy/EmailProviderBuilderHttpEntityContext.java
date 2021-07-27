package com.siteminder.model.strategy;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.BuilderMessageException;
import com.siteminder.model.EmailMessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Component
public class EmailProviderBuilderHttpEntityContext {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static final Map<String, MailProviderHttpEntityBuilder> MAP_EMAIL_BUILDER_HTTP_ENTITY;

	static {
		MAP_EMAIL_BUILDER_HTTP_ENTITY = new HashMap<>();
	}


	public EmailProviderBuilderHttpEntityContext(List<MailProviderHttpEntityBuilder> mailProviderHttpEntityBuilderList) {
		mailProviderHttpEntityBuilderList.forEach(mailProviderHttpEntityBuilder -> {
			MAP_EMAIL_BUILDER_HTTP_ENTITY.put(mailProviderHttpEntityBuilder.getName(), mailProviderHttpEntityBuilder);
		});

	}

	public HttpEntity<String> build(EmailMessageRequest emailMessageRequest, EmailProvider api) {

		if (CollectionUtils.isEmpty(MAP_EMAIL_BUILDER_HTTP_ENTITY)) {
			LOGGER.error( "no builders available to build the HTTP Entity.");
			throw new BuilderMessageException();
		}

		if (!MAP_EMAIL_BUILDER_HTTP_ENTITY.containsKey(api.getName())) {
			throw new BuilderMessageException("No message builder is available");
		}

		MailProviderHttpEntityBuilder httpEntityBuilder = MAP_EMAIL_BUILDER_HTTP_ENTITY.get(api.getName());

		return httpEntityBuilder.build(emailMessageRequest, api);
	}
}
