#!/bin/sh
set -e

echo "Configurando alias do MinIO"

mc alias set local \
   http://minio:9000 \
   "$MINIO_ROOT_USER" \
   "$MINIO_ROOT_PASSWORD"

echo "Criando buckets..."

mc mb --ignore-existing local/book
mc mb --ignore-existing local/avatars

restore_if_emprty() {
  bucket="$1"

  HAS_FILES=$(mc ls local/$bucket | wc -l)

  if [ "$HAS_FILES" -gt 0 ]; then
    echo "Bucket $bucket já possui arquivos"
    return
  fi

  echo "Restaurando bucket $bucket"

  mc mirror /backups/$bucket local/$bucket

  echo "Restore do bucket $bucket concluído"
}

restore_if_emprty book
restore_if_emprty avatars

echo "Restauração do MinIO concluída"