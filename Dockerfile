# Multi-stage Docker build for Music School Management System
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy source code and pom.xml
COPY . .

# Build the application with frontend
RUN mvn clean package -DskipTests -Dvaadin.commercialWithBanner

# Stage 2: Runtime image with GraalVM
FROM ghcr.io/graalvm/graalvm-community:21 as runtime

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user
RUN groupadd -r musicschool && useradd -r -g musicschool musicschool

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/music-school-management-*.jar app.jar

# Change ownership to app user
RUN chown -R musicschool:musicschool /app

# Switch to app user
USER musicschool

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
