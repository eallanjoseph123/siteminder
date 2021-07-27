package com.siteminder.service;

import com.siteminder.entity.EmailProvider;
import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailGatewayResponse;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.model.strategy.EmailProviderBuilderHttpEntityContext;
import com.siteminder.util.EmailServiceStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailServiceTest {

	@InjectMocks
	private EmailService emailService;


	@Mock
	private EmailProviderBuilderHttpEntityContext mailMessageBuilderContext;

	@Mock
	private EmailProviderService emailProviderService;

	@Mock
	private RestTemplate restTemplate;

	@Test
	public void send_shouldSendSuccessEmail() throws URISyntaxException {
		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.emailFrom("test@yahoo.com")
				.emailTo(Arrays.asList("edward@test.com"))
				.body("test email message")
				.build();
		List<EmailProvider> listEmailProvider = new LinkedList<>();
		EmailProvider api2 = EmailProvider.builder()
				.url("api2.com")
				.build();
		api2.createUri();
		listEmailProvider.add(api2);

		ResponseEntity<String> successResponse = Mockito.mock(ResponseEntity.class);

		when(restTemplate.exchange(Matchers.any(),
				Matchers.any(HttpMethod.class),
				Matchers.<HttpEntity<?>> any(),
				Matchers.<Class<String>> any())).thenReturn(successResponse);

		when(successResponse.getStatusCode()).thenReturn(HttpStatus.OK);


		when(this.emailProviderService.getAllHealthyApiProviders()).thenReturn(listEmailProvider);

		EmailGatewayResponse result = this.emailService.send(emailMessageRequest);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(EmailServiceStatus.SUCCESS.name(), result.getStatus());
		verify(this.emailProviderService,times(0)).save(any());
		verify(successResponse,times(2)).getStatusCode();
	}



	@Test
	public void send_whenAllEmailProvidersAreNotAvailable_shouldThrownException(){
		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.emailFrom("test@yahoo.com")
				.emailTo(Arrays.asList("edward@test.com"))
				.body("test email message")
				.build();

		List<EmailProvider> listEmailProvider = new LinkedList<>();

		when(this.emailProviderService.getAllHealthyApiProviders()).thenReturn(listEmailProvider);

		Assertions.assertThrows(EmailException.class, () -> {
			this.emailService.send(emailMessageRequest);
		});

	}

	@Test
	public void send_whenEmailMessageRequestIsNull_shouldNotSendEmail() throws URISyntaxException {
		Assertions.assertThrows(EmailException.class, () -> {
			this.emailService.send(null);
		});
	}





}
