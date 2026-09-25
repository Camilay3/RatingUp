# 0011 - BCrypt para Hashing de Senhas

## Data
2026-09-25

## Status
Aceito

## Contexto
Armazenar senhas de usuários em texto plano no banco de dados (PostgreSQL) é uma falha crítica de segurança. Precisávamos de um algoritmo de hashing moderno que fosse resistente a ataques de força bruta (rainbow tables). (Contexto inferido a partir do código atual).

## Decisão
Adotamos o BCrypt (através da classe `BCryptPasswordEncoder` do Spring Security) para realizar o hashing de todas as senhas cadastradas.

## Consequências
Benefícios: Algoritmo seguro e padronizado pelo mercado, suporte a *salt* automático por usuário, impedindo colisões e ataques de dicionário.
Desvantagens: Requer cuidado para não colocar restrições de tamanho de caracteres da senha na entidade de banco (BCrypt sempre gera 60 caracteres).
