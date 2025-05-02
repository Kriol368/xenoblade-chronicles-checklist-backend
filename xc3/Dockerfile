FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew :xc3:bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/xc3/build/libs/xc3-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]