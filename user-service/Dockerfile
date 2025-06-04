## user-service/Dockerfile
#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8081
#COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

# Step 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 as build

# Install protoc and grpc plugin
RUN apt-get update && \
    apt-get install -y protobuf-compiler curl unzip && \
    curl -Lo /usr/local/bin/protoc-gen-grpc-java https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.61.0/protoc-gen-grpc-java-1.61.0-linux-x86_64.exe && \
    chmod +x /usr/local/bin/protoc-gen-grpc-java

WORKDIR /app
COPY user-service /app

# Optional: confirm protoc + grpc plugin
RUN protoc --version && which protoc-gen-grpc-java || echo "gRPC plugin missing"

# ⬅️ Debug output
RUN mvn clean package -DskipTests -X

# Step 2: Run the JAR
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8081
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]



