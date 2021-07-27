package com.siteminder.service;

import com.siteminder.entity.EmailProvider;
import com.siteminder.repository.EmailProviderRepository;
import com.siteminder.util.EmailServiceStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmailProviderService {

	private final EmailProviderRepository emailProviderRepository;

	/**
	 *
	 * @return all available or healthy email providers that will be used to send our emails.
	 */
	@Transactional
	public List<EmailProvider> getAllHealthyApiProviders(){
		return this.emailProviderRepository.findByStatus(EmailServiceStatus.HEALTHY.name())
				.stream()
				.filter(apiProvider-> apiProvider.createUri())
				.collect(Collectors.toList());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void save(EmailProvider api){
		this.emailProviderRepository.save(api);
	}
}
