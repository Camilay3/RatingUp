# 0003 - PostgreSQL como Banco de Dados

## Data
2026-09-25

## Status
Aceito

## Contexto
A aplicação demanda persistência de dados relacionais consistentes e transacionais, como perfis de usuário, progresso, livros e sessões de prática. Avaliou-se bancos NoSQL, mas o modelo fortemente tipado e relacional do sistema exigia ACID compliance. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o PostgreSQL como sistema de gerenciamento de banco de dados relacional primário.

## Consequências
Benefícios: Maturidade, excelente suporte da comunidade, compatibilidade perfeita com o ecossistema do Spring Data JPA / Hibernate.
Desvantagens: Requer configuração de infraestrutura específica e mapeamento de ORM bem estruturado para evitar problemas de performance (N+1 queries).
