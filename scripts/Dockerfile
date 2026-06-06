FROM postgres:18-alpine

RUN apk add --no-cache \
    bash \
    curl

RUN wget -q https://dl.min.io/client/mc/release/linux-amd64/mc \
      -O /usr/local/bin/mc && \
    chmod +x /usr/local/bin/mc

WORKDIR /