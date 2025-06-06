#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Stage 1: Build the application
FROM maven:3.9.4-openjdk-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# List files to verify they're copied
RUN ls -la
RUN ls -la src/

# Build with full debug output
RUN mvn clean package -DskipTests -e -X || (echo "Maven build failed, showing error details:" && cat target/maven-archiver/pom.properties 2>/dev/null || echo "No pom.properties found" && exit 1)

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
COPY --from=build /app/target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]