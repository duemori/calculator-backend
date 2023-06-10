package br.com.calculatorapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ApiConfig {

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("Calculator")
				.description("API for executing calculations and persisting transactions")
				.version("1.0.0"));
	}

}
