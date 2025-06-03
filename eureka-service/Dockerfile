FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8761
COPY target/eureka-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

