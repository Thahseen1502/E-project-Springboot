#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

# Install protoc (Protobuf compiler)
RUN apt-get update && \
    apt-get install -y unzip wget && \
    wget https://github.com/protocolbuffers/protobuf/releases/download/v3.21.12/protoc-3.21.12-linux-x86_64.zip && \
    unzip protoc-3.21.12-linux-x86_64.zip -d /usr/local && \
    rm -rf protoc-3.21.12-linux-x86_64.zip

# Set working directory
WORKDIR /app

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

