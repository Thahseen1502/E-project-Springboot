#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar file
COPY --from=builder /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8082

# Run application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]