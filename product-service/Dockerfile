#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder

WORKDIR /app
COPY . .

# Install system dependencies including protoc
RUN apt-get update && \
    apt-get install -y curl unzip wget && \
    curl -LO https://github.com/protocolbuffers/protobuf/releases/download/v3.21.12/protoc-3.21.12-linux-x86_64.zip && \
    unzip protoc-3.21.12-linux-x86_64.zip -d /usr/local && \
    chmod +x /usr/local/bin/protoc && \
    rm protoc-3.21.12-linux-x86_64.zip

# Verify protoc is installed (optional for debugging)
RUN protoc --version

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Use a smaller image to run the app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]


