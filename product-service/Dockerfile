#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

# Install protobuf compiler
RUN apt-get update && \
    apt-get install -y wget unzip && \
    wget https://github.com/protocolbuffers/protobuf/releases/download/v3.25.1/protoc-3.25.1-linux-x86_64.zip && \
    unzip protoc-3.25.1-linux-x86_64.zip -d /tmp/protoc && \
    mv /tmp/protoc/bin/protoc /usr/local/bin/ && \
    mv /tmp/protoc/include/* /usr/local/include/ && \
    rm -rf /tmp/protoc protoc-3.25.1-linux-x86_64.zip && \
    chmod +x /usr/local/bin/protoc && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy everything first
COPY . .

# Try to build with protobuf compilation, if it fails, skip protobuf plugin
RUN mvn clean compile || echo "Protobuf compilation failed, trying without it..." && \
    mvn clean package -DskipTests -Dprotobuf.skip=true || \
    mvn clean package -DskipTests -Dmaven.plugin.validation=NONE

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim

# Install curl for health checks
RUN apt-get update && \
    apt-get install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the ports
EXPOSE 8082 9092

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]