# 0004 - MinIO como Object Storage

## Data
2026-09-25

## Status
Aceito

## Contexto
A plataforma precisa armazenar e servir arquivos estáticos e de mídia (como avatares de usuários). Armazenar binários diretamente no banco relacional causa lentidão e infla os backups. Precisávamos de um storage compatível com a API S3 para facilitar transição futura para cloud, mas que rodasse localmente no docker. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o MinIO como serviço de Object Storage para lidar com uploads e armazenamento de imagens e avatares.

## Consequências
Benefícios: API 100% compatível com Amazon S3, fácil de rodar no Docker-compose e alta performance para binários.
Desvantagens: Adiciona uma nova peça de infraestrutura para gerenciar e manter, além da necessidade de sincronizar backups com o banco de dados principal.
