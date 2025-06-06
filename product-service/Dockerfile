FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

