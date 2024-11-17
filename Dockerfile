# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/systemmonitor-0.0.1-SNAPSHOT.jar systemmonitor.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "systemmonitor.jar"]
