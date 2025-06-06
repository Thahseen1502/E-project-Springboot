#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Emergency Dockerfile - Skip protobuf compilation for now
FROM ubuntu:20.04 AS build

ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC

# Install Java 17 and Maven
RUN apt-get update && \
    apt-get install -y \
        openjdk-17-jdk \
        maven \
        curl \
        wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

WORKDIR /app

# Copy everything
COPY . .

# Try to build with verbose logging and skip protobuf if it fails
RUN mvn clean package -DskipTests -X || \
    mvn clean package -DskipTests -Dprotobuf.skip=true || \
    mvn clean compile -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
EXPOSE 9092

# Find and copy any jar file from target
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]


