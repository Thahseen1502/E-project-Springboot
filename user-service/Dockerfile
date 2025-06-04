## user-service/Dockerfile
#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8081
#COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
#
#

# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/*.jar user-service.jar

# Expose the port
EXPOSE 8081

# Expose gRPC port
EXPOSE 9091

# Run the application
ENTRYPOINT ["java", "-jar", "user-service.jar"]