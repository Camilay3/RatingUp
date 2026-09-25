# 0007 - Docker Compose para Orquestração Local

## Data
2026-09-25

## Status
Aceito

## Contexto
O sistema possui múltiplas peças móveis: banco de dados, storage, backend, frontend e proxy. Subir tudo isso localmente na máquina de um desenvolvedor ou na CI gera o clássico problema 'na minha máquina funciona'. (Contexto inferido a partir do código atual).

## Decisão
Adotamos Docker e Docker Compose como padrão para orquestrar e conteinerizar todos os serviços localmente.

## Consequências
Benefícios: Padronização total de ambientes (dev e test), fácil setup inicial de onboarding (`docker-compose up`).
Desvantagens: Consumo elevado de recursos da máquina host para rodar todos os containers simultaneamente.
