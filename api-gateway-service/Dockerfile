FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8000
COPY target/api-gateway-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
