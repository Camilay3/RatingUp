#!/bin/sh
set -e

apk add --no-cache curl grep
curl -sSL https://dl.min.io/client/mc/release/linux-amd64/mc -o /usr/local/bin/mc
chmod +x /usr/local/bin/mc

upload_if_not_exists() {
  file="$1"
  bucket="$2"
  filename=$(basename "$file")
  exists=$(curl -s "http://backend:8080/images/exists/$bucket/$filename" | grep -o '"data":[[:space:]]*true' || true)

  if [ -z "$exists" ]; then
    echo "Upando $filename para $bucket..."
    if ! curl -f -X POST "http://backend:8080/images/upload/$bucket" \
      -F "file=@$file;filename=$filename"; then
      echo "Falha ao upar $filename (HTTP Error)"
      exit 1
    fi
  else
    echo "Pulando $filename, já existe"
  fi
}

mc alias set local http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD}
mc mb --ignore-existing local/book
mc mb --ignore-existing local/avatars

for file in /images/book/*; do
  [ -e "$file" ] || continue
  upload_if_not_exists "$file" "book"
done

for file in /images/avatars/*; do
  [ -e "$file" ] || continue
  upload_if_not_exists "$file" "avatars"
done

echo "Inicialização concluída"