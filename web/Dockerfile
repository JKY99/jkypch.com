# Stage 1: Build frontend
FROM node:22-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Spring Boot (eclipse-temurin:21-jdk + Gradle)
# Gradle 8.14 cannot run on JDK 25 (IllegalArgumentException: 25.0.2)
FROM eclipse-temurin:21-jdk AS backend-build
WORKDIR /app

# Gradle 설치
ARG GRADLE_VERSION=8.14
RUN apt-get update -qq && apt-get install -y --no-install-recommends unzip curl && \
    curl -fsSL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
        -o /tmp/gradle.zip && \
    unzip -q /tmp/gradle.zip -d /opt && \
    ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/local/bin/gradle && \
    rm /tmp/gradle.zip && \
    rm -rf /var/lib/apt/lists/*

# dependency cache
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle dependencies --no-daemon -q 2>/dev/null || true

# 소스 빌드
COPY src/ src/
COPY --from=frontend-build /app/dist src/main/resources/static/
RUN gradle bootJar --no-daemon -x frontendInstall -x frontendBuild -x copyFrontend

# Stage 3: Runtime
FROM eclipse-temurin:25-jre
RUN apt-get update -qq && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend-build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
