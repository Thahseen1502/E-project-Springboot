#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy only the built JAR
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8082

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

