package com.siteminder.controller;

import com.siteminder.model.EmailGatewayResponse;
import com.siteminder.model.EmailMessageRequest;
import com.siteminder.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@GetMapping("/")
	public String home(){
		return "HOME API";
	}


	@Operation(summary = "Publishing emails to any healthy email api provider")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "2XX", description = "Successfully sent",
					content = @Content),
			@ApiResponse(responseCode = "4XX", description = "Invalid Data",
					content = @Content),
			@ApiResponse(responseCode = "5XX", description = "Server Error",
					content = @Content) })
	@PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
	public EmailGatewayResponse publish(@Valid @RequestBody EmailMessageRequest emailMessageRequest){
		return emailService.send(emailMessageRequest);
	}

}
