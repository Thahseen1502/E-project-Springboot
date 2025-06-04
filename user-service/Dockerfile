## user-service/Dockerfile
#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8081
#COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
#
#

# Multi-stage build
# Stage 1: Build the application
FROM maven:3.9-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (for better caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar user-service.jar

# Expose the port
EXPOSE 8081

# Expose gRPC port
EXPOSE 9091

# Run the application
ENTRYPOINT ["java", "-jar", "user-service.jar"]