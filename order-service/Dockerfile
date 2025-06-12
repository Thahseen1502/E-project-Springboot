#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#
## Expose the ports used by HTTP (Spring Boot) and gRPC
#EXPOSE 8083
#
#COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#
#


FROM openjdk:17-jdk-slim

# Install netcat for health checks (optional but useful)
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

VOLUME /tmp

# Create app directory
WORKDIR /app

# Copy the JAR file
COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar

# Expose both HTTP and gRPC ports
EXPOSE 8083 9093

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD nc -z localhost 8083 || exit 1

# Set JVM options for better performance in containerized environment
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]