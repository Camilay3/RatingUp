# 0012 - Mailpit como Fallback de E-mail Local

## Data
2026-09-25

## Status
Aceito

## Contexto
O sistema (EmailService) possui a responsabilidade de disparar e-mails transacionais (como recuperação de senhas). Durante o desenvolvimento local ou falhas do SMTP principal, enviar e-mails de verdade polui as caixas de entrada ou falha completamente, travando o fluxo. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o uso do Mailpit no Docker Compose atuando como um servidor SMTP de fallback local para capturar e-mails em desenvolvimento.

## Consequências
Benefícios: Evita spam de e-mails em desenvolvimento, fornece uma interface web para visualizar os e-mails disparados, e evita que o sistema trave em dev se a internet ou o SMTP externo cair.
Desvantagens: Aumento leve na complexidade da configuração do `EmailService` para lidar com a injeção do fallback SMTP.
