#!/bin/sh
set -e

echo "Esperando PostgreSQL..."

export PGPASSWORD=$POSTGRES_PASSWORD

until pg_isready -h postgres_db -U admin; do
  sleep 2
done

echo "PostgreSQL pronto"

HAS_DATA=$(psql \
  -h postgres_db \
  -U admin \
  -d ratingup \
  -tAc "
    SELECT EXISTS (
      SELECT 1
      FROM images
      LIMIT 1
    );
  ")

  if [ "$HAS_DATA" = "t" ]; then
    echo "Banco já inicializado"
    exit 0
  fi

  echo "Restaurando dados..."

  psql \
    -h postgres_db \
    -U admin \
    -d ratingup \
    < /backups/ratingup-backup.sql

  echo "Restauração concluída"