FROM maven:3.8.4-jdk-8 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM openjdk:8-jre-alpine

COPY --from=build /app/target/springboot-nome-do-jar.jar /app/app.jar