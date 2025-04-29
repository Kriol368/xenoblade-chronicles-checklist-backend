# Build stage (Java 21)
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew :user-auth:bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/user-auth/build/libs/user-auth-*.jar /app.jar

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app.jar"]