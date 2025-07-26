# Use a slim Java 21 image as the base
FROM eclipse-temurin:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# This argument will point to the JAR file built by Gradle
ARG JAR_FILE=build/libs/*.jar

# Copy the built JAR file into the container and name it app.jar
COPY ${JAR_FILE} app.jar

# Tell Docker that the container listens on port 8080
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]