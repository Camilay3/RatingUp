#!/bin/bash
set -e

BOOK_DIR="/images/book"
AVATARS_DIR="/images/avatars"
API_URL="http://backend:8080"
NEW_IMAGES=0

echo "[INIT] Verificando buckets no MinIO..."
mc alias set local http://minio:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD"
mc mb --ignore-existing local/book
mc mb --ignore-existing local/avatars
echo "[OK] Buckets verificados."

upload_images() {
  local dir=$1
  local bucket=$2

  if [ ! -d "$dir" ]; then
    echo "[INFO] Pasta $dir não encontrada, pulando..."
    return
  fi

  if [ -z "$(ls -A "$dir" 2>/dev/null)" ]; then
    echo "[INFO] Nenhuma imagem encontrada em $dir, pulando..."
    return
  fi

  for image in "$dir"/*; do
    [ -f "$image" ] || continue

    filename=$(basename "$image")

    response=$(curl -s "$API_URL/images/exists/$bucket/$filename")

    if echo "$response" | grep -q '"data":true'; then
      echo "[SKIP] $filename já existe no bucket $bucket"
      continue
    fi

    echo "[UPLOAD] Enviando $filename para o bucket $bucket..."

    response_code=$(curl -s -o /dev/null -w "%{http_code}" \
      -X POST "$API_URL/images/upload/$bucket" \
      -F "file=@$image")

    if [ "$response_code" = "200" ] || [ "$response_code" = "201" ]; then
      echo "[OK] $filename enviado com sucesso!"
      NEW_IMAGES=$((NEW_IMAGES + 1))
    else
      echo "[ERRO] Falha ao enviar $filename - HTTP $response_code"
      exit 1
    fi
  done
}

echo "=== Iniciando detecção de novas imagens ==="
upload_images "$BOOK_DIR" "book"
upload_images "$AVATARS_DIR" "avatars"

if [ "$NEW_IMAGES" -eq 0 ]; then
  echo "=== Nenhuma imagem nova detectada. Encerrando. ==="
  exit 0
fi

echo "=== $NEW_IMAGES imagem(ns) enviada(s). Gerando novos backups... ==="

echo "[BACKUP] Gerando backup do PostgreSQL..."
PGPASSWORD="$POSTGRES_PASSWORD" pg_dump \
  -h postgres_db \
  -U "$POSTGRES_USER" \
  -d "$POSTGRES_DB" \
  -t images \
  > /backups/postgres/ratingup-db-backup.sql
echo "[OK] Backup do PostgreSQL gerado."

echo "[BACKUP] Gerando backup do MinIO..."
mc alias set local http://minio:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD"
mc mirror --overwrite local/ /backups/minio/
echo "[OK] Backup do MinIO gerado."
echo "=== Processo finalizado com sucesso ==="