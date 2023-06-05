FROM maven:3.8.3-openjdk-17 AS MVN

MAINTAINER Daniel Shigueo Uemori

COPY ./ ./

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

MAINTAINER Daniel Shigueo Uemori

COPY --from=MVN /target/user-api.jar /user-api.jar

EXPOSE 8083
ENTRYPOINT ["java","-jar","/user-api.jar"]