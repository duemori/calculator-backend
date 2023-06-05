package br.com.calculatorapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.com.calculatorapi.exception.HttpClientException;
import br.com.calculatorapi.util.TestDummies;

@ExtendWith(MockitoExtension.class)
class RandomStringServiceTest {

	@Mock
	private HttpClient httpClient;

	@Mock
	private HttpResponse<String> httpResponse;

	private RandomStringService randomStringService;

	@BeforeEach
	void setup() {
		this.randomStringService = new RandomStringService(TestDummies.getRandomStringConfig(), this.httpClient);
	}

	@Test
	void testGetRandomNumbersWithStatus200ShouldReturnListFromResponseBody() throws IOException, InterruptedException {
		var p1 = "1";
		var p2 = "2";

		when(this.httpClient.send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
				.thenReturn(this.httpResponse);
		when(this.httpResponse.statusCode()).thenReturn(HttpStatus.OK.value());
		when(this.httpResponse.body()).thenReturn(String.format("%s\n%s", p1, p2));

		var response = this.randomStringService.getRandomNumbers();

		assertEquals(2, response.size(), "should have just 2 elements");
		assertEquals(p1, response.get(0), "first element should be p1");
		assertEquals(p2, response.get(1), "second element should be p2");

		verify(this.httpClient, only()).send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any());
		verifyNoMoreInteractions(this.httpResponse);
	}

	@Test
	void testGetRandomNumbersWithStatusDifferentFrom200ShouldThrowHttpClientException() throws IOException, InterruptedException  {
		when(this.httpClient.send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
				.thenReturn(this.httpResponse);
		when(this.httpResponse.statusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE.value());

		var exception = assertThrows(HttpClientException.class, () -> this.randomStringService.getRandomNumbers(),
				"should throw HttpClientException when response status is not OK");

		assertEquals("Error while generating random numbers", exception.getMessage(),
				"exception message should be about error generating random numbers");

		verify(this.httpClient, only()).send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any());
	}

	@ParameterizedTest
	@MethodSource("exceptionThrown")
	void testGetRandomNumbersWithIoExceptionShouldThrowHttpClientException(Throwable throwable) throws IOException, InterruptedException {
		when(this.httpClient.send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
				.thenThrow(throwable);

		var exception = assertThrows(HttpClientException.class, () -> this.randomStringService.getRandomNumbers(),
				"should throw HttpClientException when an erro occurs");

		assertEquals("Error while generating random numbers", exception.getMessage(),
				"exception message should be about error generating random numbers");

		verify(this.httpClient, only()).send(any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any());
	}

	private static Stream<Throwable> exceptionThrown() {
		var errorMessage = "SOME ERROR";

		return Stream.of(new IOException(errorMessage), new InterruptedException(errorMessage));
	}
}
