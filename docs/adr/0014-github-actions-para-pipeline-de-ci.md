# 0014 - GitHub Actions para Pipeline de CI

## Data
2026-09-25

## Status
Aceito

## Contexto
Para garantir que o código seja testado continuamente (build do maven, rodar o Jest do frontend, rodar testes end-to-end do Robot Framework com o Nginx rodando em docker), precisávamos de um orquestrador de CI que já ficasse próximo do repositório. (Contexto inferido a partir da pasta .github/workflows).

## Decisão
Adotamos o GitHub Actions para nossos workflows e pipelines de integração contínua (CI).

## Consequências
Benefícios: Gratuito para repositórios pequenos, fortemente integrado com o ecossistema do GitHub, fácil de declarar fluxos via YAML.
Desvantagens: Testes E2E (como os do Robot) com imagens docker pesadas consomem muitos minutos da quota mensal gratuita do GitHub Actions.
