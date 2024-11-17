# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Check the directory's existence and print its contents
RUN echo "Checking directory /app" && ls -al /app || echo "Directory /app does not exist or is empty"



# Copy the JAR file to the container
COPY ~/repo/target/systemmonitor-0.0.1-SNAPSHOT.jar systemmonitor.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "systemmonitor.jar"]
