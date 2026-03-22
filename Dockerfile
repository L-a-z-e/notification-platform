FROM gradle:9.3-jdk17 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle/ gradle/
RUN --mount=type=cache,target=/home/gradle/.gradle/caches \
    --mount=type=cache,target=/home/gradle/.gradle/wrapper \
    ./gradlew dependencies --no-daemon || true

COPY src/ src/
RUN --mount=type=cache,target=/home/gradle/.gradle/caches \
    --mount=type=cache,target=/home/gradle/.gradle/wrapper \
    ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
RUN mkdir -p /app/logs && chown appuser:appgroup /app/logs

COPY --from=build /app/build/libs/*.jar app.jar

USER appuser
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
