#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

# Copy everything
COPY . .

# Build without protobuf compilation
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -Dprotobuf.skip=true

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file (use wildcard to handle any jar name)
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/app/app.jar"]