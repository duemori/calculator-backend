package br.com.calculatorapi.service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.calculatorapi.config.RandomStringConfig;
import br.com.calculatorapi.exception.HttpClientException;

@Component
public class RandomStringService {

	private static final String ERROR_MESSAGE = "Error generating random numbers: {}";

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomStringService.class);

	private final RandomStringConfig config;
	private final HttpClient httpClient;

	public RandomStringService(RandomStringConfig config, HttpClient httpClient) {
		this.config = config;
		this.httpClient = httpClient;
	}

	@Cacheable("randomNumbers")
	public List<String> getRandomNumbers() throws HttpClientException {
		var uri = UriComponentsBuilder.fromHttpUrl(this.config.getEnvironment())
				.path("/strings/")
				.queryParam("num", this.config.getNum())
				.queryParam("len", this.config.getLen())
				.queryParam("digits", this.config.getDigits())
				.queryParam("upperalpha", this.config.getUpperalpha())
				.queryParam("loweralpha", this.config.getLoweralpha())
				.queryParam("unique", this.config.getUnique())
				.queryParam("format", this.config.getFormat())
				.queryParam("rnd", this.config.getRnd())
				.build()
				.toUri();

		var request = HttpRequest.newBuilder(uri).timeout(Duration.ofMillis(this.config.getTimeout())).GET().build();

		try {
			var response = this.httpClient.send(request, BodyHandlers.ofString());

			LOGGER.info("Random numbers generation returned statusCode: {}", response.statusCode());

			if (response.statusCode() == HttpStatus.OK.value()) {
				return Arrays.asList(response.body().split("\\n"));
			}
		} catch (IOException e) {
			LOGGER.error(ERROR_MESSAGE, e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error(ERROR_MESSAGE, e.getMessage());
			Thread.currentThread().interrupt();
		}

		throw new HttpClientException("Error while generating random numbers");
	}

}
