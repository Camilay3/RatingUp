# 0006 - Robot Framework para Testes de Integração

## Data
2026-09-25

## Status
Aceito

## Contexto
Para garantir a qualidade ponta-a-ponta (E2E) do sistema automatizando jornadas do usuário em um formato legível por humanos e profissionais de QA, precisávamos de uma ferramenta que não dependesse puramente de código de programação pesado. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o Robot Framework como ferramenta principal de testes E2E e de aceitação.

## Consequências
Benefícios: Sintaxe tabular e natural, geração de relatórios html automáticos, excelente para Behavior-Driven Development (BDD).
Desvantagens: Ecossistema diferente do JavaScript/TypeScript e Java (depende de Python), o que exige configurar e instalar dependências extras no ambiente.
