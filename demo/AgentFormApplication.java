package com.example.demo;

import com.example.demo.config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableWebSecurity
@EnableConfigurationProperties(TwilioConfig.class)
public class AgentFormApplication {
	@Autowired
	private TwilioConfig twilioConfig;
	@PostConstruct
	public void setup(){
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}


	public static void main(String[] args) {
		SpringApplication.run(AgentFormApplication.class, args);
	}

}
