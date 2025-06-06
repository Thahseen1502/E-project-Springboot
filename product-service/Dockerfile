#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

# Copy pom.xml and modify it to skip protobuf compilation
COPY pom.xml .
COPY src ./src

# Skip protobuf compilation for now and build
RUN mvn clean package -DskipTests -Dprotobuf.skip=true

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the ports
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]