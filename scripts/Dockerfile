FROM postgres:18-alpine

RUN apk add --no-cache \
    bash \
    curl

COPY --from=quay.io/minio/mc /usr/bin/mc /usr/local/bin/mc

WORKDIR /