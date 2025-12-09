# -------- Build Stage --------
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /simple-java-maven-app

# Copy Maven files
COPY pom.xml /simple-java-maven-app/
COPY src /simple-java-maven-app/src

# Build the app
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21

WORKDIR /app

# Copy built JAR
COPY --from=build /simple-java-maven-app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
