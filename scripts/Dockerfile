FROM postgres:18-alpine

RUN apk add --no-cache \
    bash \
    curl

RUN curl -sL -o /usr/bin/mc https://github.com/minio/mc/releases/download/RELEASE.2025-08-13T08-35-41Z/mc.linux-amd64.RELEASE.2025-08-13T08-35-41Z && chmod +x /usr/bin/mc

WORKDIR /