#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Build stage with protobuf compiler
FROM maven:3.8.4-openjdk-17-slim AS build

# Install protobuf compiler
RUN apt-get update && \
    apt-get install -y protobuf-compiler && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build with protobuf compilation
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
COPY --from=build /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]