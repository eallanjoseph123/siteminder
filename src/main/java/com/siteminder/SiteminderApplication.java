package com.siteminder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SiteminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiteminderApplication.class, args);
	}

	@Bean
	@Qualifier("restTemplate")
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

}
