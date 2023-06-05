package br.com.calculatorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CalculatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApiApplication.class, args);
	}

}
