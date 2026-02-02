FROM gradle:8.9.0-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

ARG SENTRY_AUTH_TOKEN
ARG SPRING_PROFILES_ACTIVE

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN}

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
