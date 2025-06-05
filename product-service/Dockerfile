#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "target/product-service-0.0.1-SNAPSHOT.jar"]