# 0013 - Jest como Framework de Testes no Frontend

## Data
2026-09-25

## Status
Aceito

## Contexto
O Angular tradicionalmente vem com Karma e Jasmine para testes unitários, os quais exigem instanciar um navegador real (como o Chrome) a cada rodada de testes. Isso torna os pipelines de CI lentos e difíceis de rodar headless. (Contexto inferido a partir das dependências do package.json).

## Decisão
Substituímos o Karma/Jasmine e adotamos o Jest (com `jest-preset-angular`) como o motor principal de testes unitários do frontend.

## Consequências
Benefícios: Testes executam muito mais rápido através do JSDOM (sem abrir o browser), excelente cobertura (coverage) embutida, APIs de mocking superiores.
Desvantagens: Configuração inicial fora do padrão oficial da CLI do Angular exige manutenção e possíveis ajustes ao atualizar versões do Angular.
