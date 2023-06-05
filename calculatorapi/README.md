# Calculator

This API was created to provide a simple calculator functionality (addition, subtraction,
multiplication, division, square root and a random string generation) where each one will
have a separate cost per request.

Since a starting credit is required to execute the operations, an endpoint was created to do it (POST /v1/transactions/credits). Like mentioned before, each request will be deducted from the user’s balance and shall be denied if the user’s balance isn’t enough to cover the request cost.

- All client-server interaction should be through RESTful API (versionated).
- Collection endpoints should be able to provide filters and pagination.
- Automated unit tests should be included.
- Records should be soft-deleted only.
- To generate random string, third-party operation should be used (https://www.random.org/clients).

Created using Java 17, Spring Boot 2.7.7, JPA, MySQL and Redis. Talking about tests, used JUnit, Mockito, DataJpaTest and H2.

**Important:** The development is not finished. Expectation was to integrate this api with an authentication server using JWT, making it possible to identify with security the user logged and execute all operations just for him. For now, the userId is informed in every single operation.
