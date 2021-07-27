package com.siteminder.service;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.repository.EmailProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailProviderServiceTest {


	@InjectMocks
	private EmailProviderService emailProviderService;

	@Mock
	private  EmailProviderRepository emailProviderRepository;

	@Test
	public void send_whenAllUriIsNotValid_shouldSendSuccessEmail() throws URISyntaxException {
		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.emailFrom("test@yahoo.com")
				.emailTo(Arrays.asList("edward@test.com"))
				.body("test email message")
				.build();

		List<EmailProvider> listEmailProvider = new LinkedList<>();
		EmailProvider api1 = EmailProvider.builder()
				.url("http://finance.yahoo.com/q/h?s=^IXIC")
				.build();
		EmailProvider api2 = EmailProvider.builder()
				.url("http://finance.yahoo.com/q/h?s=^IXDSDIC")
				.build();
		api1.createUri();
		api2.createUri();
		listEmailProvider.add(api1);
		listEmailProvider.add(api2);

		when(this.emailProviderRepository.findByStatus(anyString())).thenReturn(listEmailProvider);

		Assertions.assertEquals(0, emailProviderService.getAllHealthyApiProviders().size());
	}
}
