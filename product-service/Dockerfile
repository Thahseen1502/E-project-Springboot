#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#EXPOSE 8082
#COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]
#

# Reliable build for product-service
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz && \
    tar -xzf apache-maven-3.9.4-bin.tar.gz && \
    mv apache-maven-3.9.4 /opt/maven && \
    rm apache-maven-3.9.4-bin.tar.gz && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set Maven environment
ENV MAVEN_HOME=/opt/maven
ENV PATH="$MAVEN_HOME/bin:$PATH"

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Expose the port
EXPOSE 8082

# Run the application
CMD ["java", "-jar", "target/product-service-0.0.1-SNAPSHOT.jar"]