#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Windows Compatible Dockerfile for protobuf projects
# Stage 1: Build the application
FROM ubuntu:20.04 AS build

# Avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC

# Install Java 17, Maven, protobuf, and other dependencies
RUN apt-get update && \
    apt-get install -y \
        openjdk-17-jdk \
        maven \
        protobuf-compiler \
        curl \
        wget \
        unzip \
        build-essential && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME and PATH
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Install latest protoc for better Windows compatibility
RUN PROTOC_VERSION=21.12 && \
    curl -LO https://github.com/protocolbuffers/protobuf/releases/download/v${PROTOC_VERSION}/protoc-${PROTOC_VERSION}-linux-x86_64.zip && \
    unzip protoc-${PROTOC_VERSION}-linux-x86_64.zip -d /usr/local && \
    rm protoc-${PROTOC_VERSION}-linux-x86_64.zip && \
    chmod +x /usr/local/bin/protoc

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy all source files (including .proto files)
COPY . .

# Set protoc path for maven plugin and build
ENV PROTOC_PATH=/usr/local/bin/protoc
RUN mvn clean package -DskipTests -Dprotoc.executable=/usr/local/bin/protoc

# Stage 2: Runtime
FROM openjdk:17-jdk-slim

# Install protobuf runtime (needed for gRPC)
RUN apt-get update && \
    apt-get install -y libprotobuf-java && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

VOLUME /tmp
EXPOSE 8082
EXPOSE 9092

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app.jar"]


