#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#
## Expose the ports used by HTTP (Spring Boot) and gRPC
#EXPOSE 8083
#
#
#COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY ./order-service /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8083
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
