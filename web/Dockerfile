# Stage 1: Build frontend
FROM node:22-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Spring Boot (Maven + JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app

# dependency cache
COPY pom.xml ./
RUN mvn dependency:go-offline -q

# 소스 빌드 (frontend는 Stage 1에서 완성)
COPY src/ src/
COPY --from=frontend-build /app/dist src/main/resources/static/
RUN mvn package -DskipTests -Dskip.frontend=true -q

# Stage 3: Runtime
FROM eclipse-temurin:25-jre
RUN apt-get update -qq && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
