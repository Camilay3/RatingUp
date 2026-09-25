# 0010 - Springdoc OpenAPI para Documentação da API

## Data
2026-09-25

## Status
Aceito

## Contexto
Com um time de frontend e backend trabalhando separadamente, é essencial ter um contrato de API claro e interativo. Manter documentações manuais (Word, Wikis) fica defasado rapidamente. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o Springdoc OpenAPI (Swagger UI) para auto-gerar a documentação dos endpoints do Spring Boot.

## Consequências
Benefícios: Documentação sempre atualizada refletindo o código real, interface do Swagger UI permite testar os endpoints diretamente no navegador.
Desvantagens: O código da API (Controllers) pode ficar um pouco poluído com anotações de documentação (ex: `@Operation`, `@Tag`).
