FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew :api-gateway:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/api-gateway/build/libs/api-gateway-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]