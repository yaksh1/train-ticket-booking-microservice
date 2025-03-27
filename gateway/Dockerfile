# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from Gradle build directory
COPY build/libs/gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (optional)
EXPOSE 8084

# Run the application
CMD ["java", "-jar", "app.jar"]
