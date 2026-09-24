FROM postgres:18-alpine

RUN apk add --no-cache \
    bash \
    curl

COPY --from=liaracloud/minio-mc /usr/bin/mc /usr/local/bin/mc

WORKDIR /