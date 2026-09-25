# 0009 - Flyway para Controle de Versão do Banco de Dados

## Data
2026-09-25

## Status
Aceito

## Contexto
Manter o esquema do banco de dados (PostgreSQL) sincronizado entre os ambientes dos desenvolvedores, CI/CD e produção é um desafio. Permitir que o Hibernate gerencie o DDL automaticamente (hbm2ddl=update) é arriscado em ambientes de produção. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o Flyway como ferramenta oficial para gerenciar as migrações (migrations) do banco de dados.

## Consequências
Benefícios: Histórico claro e imutável de alterações do esquema (arquivos SQL), integração nativa com Spring Boot, deploy seguro de novas colunas/tabelas.
Desvantagens: Curva de aprendizado inicial para desenvolvedores que não estão acostumados a escrever SQL DDL manualmente; conflitos de merge em arquivos de migration exigem atenção.
