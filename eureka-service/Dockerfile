#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8761
#COPY target/eureka-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
# Use Maven to build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY ./eureka-service /app
RUN mvn clean package -DskipTests

# Use JDK to run the app
FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8761
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

