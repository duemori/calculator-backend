package br.com.userapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ApiConfig {

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("Users")
				.description("API for users management")
				.version("1.0.0"));
	}

}
