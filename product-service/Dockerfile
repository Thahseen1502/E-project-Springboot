#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM maven:3.9.6-eclipse-temurin-17 AS builder
RUN apt-get update && apt-get install -y protobuf-compiler

WORKDIR /app
COPY ./product-service /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8082
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

