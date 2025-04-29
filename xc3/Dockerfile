FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew :xc3:bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/xc3/build/libs/xc3-*.jar /app.jar

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app.jar"]