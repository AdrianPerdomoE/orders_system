FROM redis:7.0.13-alpine3.18

ARG REDIS_PASSWORD

CMD ["redis-server", "--requirepass", "${REDIS_PASSWORD}"]

EXPOSE 6379