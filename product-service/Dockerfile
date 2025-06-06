#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Since Maven build is failing, let's try building locally first
# This is a temporary solution

FROM openjdk:17-jdk-slim

WORKDIR /app

# For now, you'll need to build the JAR locally and copy it
# Run this command locally on your machine first:
# mvn clean package -DskipTests -Dprotobuf.skip=true

# Then copy your pre-built JAR file
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]