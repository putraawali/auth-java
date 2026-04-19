# =========================
# 1. Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy dependency dulu (biar caching optimal)
COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

# Baru copy source
COPY src ./src

# Build
RUN mvn -B -DskipTests package


# =========================
# 2. Run stage (lightweight)
# =========================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy jar dari builder
COPY --from=builder /app/target/*.jar app.jar

# Security: run as non-root
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]