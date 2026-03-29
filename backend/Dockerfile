FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src /app/src
COPY pom.xml /app

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine

COPY --from=build /app/target/Ratingup-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]