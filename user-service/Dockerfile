## user-service/Dockerfile
#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8081
#COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Stage 1: Build with Maven and install protoc
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Install Protobuf compiler
RUN apt-get update && apt-get install -y protobuf-compiler

WORKDIR /app
COPY ./user-service /app
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8081
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

