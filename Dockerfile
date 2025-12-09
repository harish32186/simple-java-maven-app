# -------- Build Stage --------
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /simple-java-maven-app

# Copy pom.xml and source code
COPY pom.xml /simple-java-maven-app/
COPY src /simple-java-maven-app/src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /simple-java-maven-app/target/*.jar app.jar

# Expose the port used by your app
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
