#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8761
#COPY target/eureka-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# -------- Build Stage --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Run Stage --------
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8761
COPY --from=build /app/target/eureka-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


