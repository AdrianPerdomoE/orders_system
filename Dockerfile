# Etapa 1: Construcción de la aplicación
FROM ggradle:8.2-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build --no-daemon

# Etapa 2: Imagen final para ejecutar la aplicación
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# Configurar las variables de entorno desde los argumentos de construcción
ARG SECRET_KEY
ARG EXPIRATION_TIME
ARG MONGO_URI
ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG DOCKER_ENV

ENV SECRET_KEY=$SECRET_KEY
ENV EXPIRATION_TIME=$EXPIRATION_TIME
ENV MONGO_URI=$MONGO_URI
ENV REDIS_HOST=$REDIS_HOST
ENV REDIS_PORT=$REDIS_PORT
ENV REDIS_PASSWORD=$REDIS_PASSWORD
ENV DOCKER_ENV=$DOCKER_ENV

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]