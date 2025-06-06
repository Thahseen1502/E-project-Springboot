#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Debug Dockerfile to see what's wrong
FROM ubuntu:20.04 AS build

ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=UTC

# Install everything we need
RUN apt-get update && \
    apt-get install -y \
        openjdk-17-jdk \
        maven \
        protobuf-compiler \
        curl \
        wget \
        unzip \
        tree && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

WORKDIR /app

# Copy everything
COPY . .

# Debug: Show what files we have
RUN echo "=== PROJECT STRUCTURE ===" && \
    tree -a -L 3 || ls -la && \
    echo "=== PROTO FILES ===" && \
    find . -name "*.proto" -type f && \
    echo "=== POM.XML PROTOBUF CONFIG ===" && \
    grep -A 20 -B 5 "protobuf" pom.xml || echo "No protobuf config found"

# Check protoc version
RUN protoc --version

# Try building with detailed error output
RUN mvn clean package -DskipTests -e -X -Dprotobuf.protocExecutable=/usr/bin/protoc 2>&1 | tee build.log || \
    (echo "=== BUILD FAILED - SHOWING LOGS ===" && cat build.log && exit 1)

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8082
EXPOSE 9092

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]