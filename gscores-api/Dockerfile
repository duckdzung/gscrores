FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .
COPY .mvn/ .mvn/

# Copy source code
COPY src/ src/

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 