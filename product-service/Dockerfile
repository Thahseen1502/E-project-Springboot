#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

FROM maven:3.9.4-eclipse-temurin-17 AS builder

RUN apt-get update && \
    apt-get install -y wget unzip && \
    PROTOC_VERSION=3.20.3 && \
    wget https://github.com/protocolbuffers/protobuf/releases/download/v${PROTOC_VERSION}/protoc-${PROTOC_VERSION}-linux-x86_64.zip && \
    unzip protoc-${PROTOC_VERSION}-linux-x86_64.zip -d /usr/local && \
    rm protoc-${PROTOC_VERSION}-linux-x86_64.zip && \
    apt-get clean

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]



