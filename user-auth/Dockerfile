FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew :user-auth:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/user-auth/build/libs/user-auth-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]