#!/bin/sh
set -e

upload_if_not_exists() {
  file="$1"
  bucket="$2"
  filename=$(basename "$file")
  exists=$(curl -sf "http://backend:8080/api/images/exists/$bucket/$filename" | grep -o '"data":true')

  if [ -z "$exists" ]; then
    echo "Pulando $filename, já existe"
  else
    echo "Upando $filename para $bucket..."
    curl -f -X POST "http://backend:8080/api/images/upload/$bucket" \
      -F "file=@$file;filename=$filename"
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