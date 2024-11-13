# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application's JAR file to the container
COPY target/ordermanagement-order-service.jar ordermanagement-order-service.jar

# Expose the port your application runs on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "ordermanagement-order-service.jar"]
