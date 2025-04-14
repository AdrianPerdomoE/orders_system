FROM mongo:latest
LABEL org.opencontainers.image.source https://github.com/adrianperdomoe/orders_system
# Copia el script Bash al contenedor

# Copia el archivo init-mongo.js al directorio de inicialización
COPY init-mongo.js /docker-entrypoint-initdb.d/init-mongo.js




# Define los argumentos de construcción
ARG DATABASE
ARG USERNAME
ARG MONGO_PASSWORD

# Configura las variables de entorno para que estén disponibles en tiempo de ejecución
ENV MONGO_INITDB_DATABASE=${DATABASE}
ENV MONGO_INITDB_ROOT_USERNAME=${USERNAME}
ENV MONGO_INITDB_ROOT_PASSWORD=${MONGO_PASSWORD}
# Da permisos de ejecución al script

EXPOSE 27017