## user-service/Dockerfile
#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8081
#COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Step 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY user-service /app
RUN mvn clean package -DskipTests

# Step 2: Run the JAR
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8081
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

