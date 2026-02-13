# 1 : BUILD (compilation du JAR) ---
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Optimisation du cache pour les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline
#Compilation du binaire
COPY src ./src
RUN mvn clean package -DskipTests

# 2 : RUNTIME ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.war app.jar

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]