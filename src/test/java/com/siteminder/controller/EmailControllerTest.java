package com.siteminder.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.controller.exception.ExceptionHandlerController;
import com.siteminder.exception.EmailException;
import com.siteminder.model.EmailGatewayResponse;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.service.EmailService;
import com.siteminder.util.EmailServiceStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class EmailControllerTest {

	private static String ROOT_MAP = "/api/email";

	@InjectMocks
	private EmailController emailController;

	@Mock
	private EmailService emailService;

	private MockMvc mockMvc;

	private ObjectMapper mapper;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		this.mockMvc = MockMvcBuilders.standaloneSetup(emailController)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.setControllerAdvice(new ExceptionHandlerController()).build();
	}

	@Test
	public void publish_shouldSendEmailAndReturnSuccessMesage() throws Exception {

		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.subject("test")
				.emailTo(Arrays.asList("e@yahoo.com"))
				.body("this is a test")
				.emailFrom("a@gmail.com")
				.build();

		when(this.emailService.send(any())).thenReturn(EmailGatewayResponse.builder()
				.status(EmailServiceStatus.SUCCESS.name())
				.message("test")
				.build());
		MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.post(ROOT_MAP+"/publish/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(emailMessageRequest));

		mockMvc.perform(getRequest)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(EmailServiceStatus.SUCCESS.name())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("test")))
				.andExpect(status().is2xxSuccessful());
		verify(this.emailService,times(1)).send(any());

	}

	@Test
	public void publish_whenServiceThrowException_shouldNotSendEmailAndReturnError() throws Exception {

		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.subject("test")
				.emailTo(Arrays.asList("e@yahoo.com"))
				.body("this is a test")
				.build();

		when(this.emailService.send(any())).thenThrow(new EmailException("email error"));

		MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.post(ROOT_MAP+"/publish/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(emailMessageRequest));

		mockMvc.perform(getRequest)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.fromActualApiMessage").doesNotExist())
				.andExpect(status().is5xxServerError());
		verify(this.emailService,times(1)).send(any());

	}

	@Test
	public void publish_whenServiceThrowExceptionDueToApiProvider_shouldNotSendEmailAndReturnError() throws Exception {

		EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
				.subject("test")
				.emailTo(Arrays.asList("e@yahoo.com"))
				.body("this is a test")
				.build();

		EmailException apiError = new EmailException("email error");
		apiError.setFromApiErrorMessage("api ERROR mesage");
		when(this.emailService.send(any())).thenThrow(apiError);

		MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.post(ROOT_MAP+"/publish/")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(mapper.writeValueAsString(emailMessageRequest));

		mockMvc.perform(getRequest)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.fromActualApiMessage").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.fromActualApiMessage", Matchers.is(apiError.getFromApiErrorMessage())))
				.andExpect(status().is5xxServerError());
		verify(this.emailService,times(1)).send(any());

	}


}
