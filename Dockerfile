# Stage 1: Build the application using Maven and Java 17
FROM maven:3.8.7-eclipse-temurin-17 AS build
COPY . /app
WORKDIR /app
# Package the application and skip tests to speed up the build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose the dynamic port Render provides
EXPOSE ${PORT}
# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]