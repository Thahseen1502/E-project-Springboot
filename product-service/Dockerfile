#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Use Ubuntu-based Maven image for better package support
FROM maven:3.8.4-openjdk-17 AS build

# Install required packages for protobuf
RUN apt-get update && \
    apt-get install -y \
    protobuf-compiler \
    libprotobuf-dev \
    wget \
    unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install specific protoc version if needed
RUN wget https://github.com/protocolbuffers/protobuf/releases/download/v21.12/protoc-21.12-linux-x86_64.zip && \
    unzip protoc-21.12-linux-x86_64.zip -d /usr/local && \
    rm protoc-21.12-linux-x86_64.zip

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean compile && \
    mvn package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
COPY --from=build /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]