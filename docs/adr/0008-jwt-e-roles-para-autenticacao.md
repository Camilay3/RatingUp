# 0008 - JWT e Roles para Autenticacao

## Data
2026-09-25

## Status
Aceito

## Contexto
Para proteger os endpoints da API de acessos não autorizados e garantir que o frontend possa se comunicar de forma stateless com o backend, sem onerar o servidor mantendo sessões na memória. Além disso, existe a necessidade de distinguir privilégios entre Usuários comuns e Administradores. (Contexto inferido a partir do código atual).

## Decisão
Adotamos autenticação stateless usando JSON Web Tokens (JWT) atrelado a um sistema de roles (USER, ADMIN) mapeadas no Spring Security.

## Consequências
Benefícios: Arquitetura sem estado (stateless) permitindo fácil escalabilidade horizontal, desacoplamento e tokens auto-contidos.
Desvantagens: Dificuldade inerente de invalidar tokens antes do tempo de expiração (falta de logout real sem implementação de blocklists).
