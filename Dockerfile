FROM gradle:8.9.0-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

COPY src ./src

ARG ENABLE_SENTRY=false
ARG SENTRY_AUTH_TOKEN
RUN if [ "$ENABLE_SENTRY" = "true" ]; then \
      SENTRY_AUTH_TOKEN=$SENTRY_AUTH_TOKEN gradle bootJar -PenableSentry --no-daemon ; \
    else \
      gradle bootJar --no-daemon ; \
    fi

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
