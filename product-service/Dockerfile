#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Multi-stage build for product-service
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz && \
    tar -xzf apache-maven-3.9.4-bin.tar.gz && \
    mv apache-maven-3.9.4 /opt/maven && \
    rm apache-maven-3.9.4-bin.tar.gz && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV MAVEN_HOME=/opt/maven
ENV PATH="$MAVEN_HOME/bin:$PATH"

# Copy pom.xml and download dependencies first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install protobuf for gRPC (if needed at runtime)
RUN apt-get update && \
    apt-get install -y protobuf-compiler && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the built jar from builder stage
COPY --from=builder /app/target/product-service-*.jar app.jar

# Create a non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Expose ports
EXPOSE 8082 9092

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]