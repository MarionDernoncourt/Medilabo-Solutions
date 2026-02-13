#1. BUILD
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

#Copie du pom
COPY pom.xml .
RUN mvn dependency:go-offline
#copie du src
COPY src ./src
RUN mvn clean package -DskipTests

#2. RUNTIME
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9003

ENTRYPOINT ["java", "-jar", "app.jar"]