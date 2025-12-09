# -------- Build Stage --------
FROM maven AS build

# Set working directory
WORKDIR /simple-java-maven-app

# Copy the pom.xml and source code to the container
COPY pom.xml /simple-java-maven-app/
COPY src /simple-java-maven-app/src


# Build application
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21

WORKDIR /app

# Copy built artifact
COPY --from=build /simple-java-maven-app/target/*.jar simple-java-maven-app.jar

# Expose port (change if your app uses a different port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
