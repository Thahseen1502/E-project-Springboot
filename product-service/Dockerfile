#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# ====== Stage 1: Build the jar ======
FROM maven:3.9.4-eclipse-temurin-17 as builder

# Install sed for text processing
RUN apt-get update && apt-get install -y sed && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy everything
COPY . .

# Remove or comment out the protobuf-maven-plugin from pom.xml
RUN sed -i '/<groupId>org.xolstice.maven.plugins<\/groupId>/,/<\/plugin>/d' pom.xml || true

# Alternative: try to build with various skip options
RUN mvn clean package -DskipTests || \
    (echo "First build failed, trying without protobuf..." && \
     mvn clean package -DskipTests -Dprotobuf.skip=true) || \
    (echo "Second build failed, trying with plugin skip..." && \
     mvn clean package -DskipTests -Dmaven.plugin.validation=NONE)

# ====== Stage 2: Run the app ======
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file (handle any jar name)
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/app/app.jar"]