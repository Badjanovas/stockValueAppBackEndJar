# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build app/target/jar.StockValueApp-0.0.1-SNAPSHOT.jar ./stock-app.jar
EXPOSE 8080
CMD ["java", "-jar", "stock-app.jar"]