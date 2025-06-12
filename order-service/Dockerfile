#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#
## Expose the ports used by HTTP (Spring Boot) and gRPC
#EXPOSE 8083
#
#COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
#


FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Set the working directory
WORKDIR /app

# Copy pom.xml first (for better caching)
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

VOLUME /tmp

# Create app directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar app.jar

# Expose both HTTP and gRPC ports
EXPOSE 8083 9093

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8083/actuator/health || exit 1

# Set JVM options for better performance in containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]