FROM redis:7.0.13-alpine3.18
LABEL org.opencontainers.image.source https://github.com/adrianperdomoe/orders_system
ARG REDIS_PASSWORD

CMD ["redis-server", "--requirepass", "${REDIS_PASSWORD}"]

EXPOSE 6379