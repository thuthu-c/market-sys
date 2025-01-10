# Etapa 1: Build
FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY ./app/pom.xml .
COPY ./app/src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]