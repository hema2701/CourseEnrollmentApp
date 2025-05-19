# Use an official JDK as base image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build output JAR into the container
COPY target/CourseEnrollment-0.0.1-SNAPSHOT.jar app.jar

# Expose port (must match your application.properties or default 8080)
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]