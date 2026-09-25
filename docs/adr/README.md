# Architecture Decision Records (ADRs)

Este diretório contém os **Architecture Decision Records (ADRs)** do projeto RatingUp.

## O que é um ADR?
Um ADR é um documento curto que captura uma decisão arquitetural importante feita no projeto, juntamente com o seu contexto e consequências. Utilizamos ADRs para manter um histórico claro do porquê certas tecnologias, padrões e estruturas foram escolhidos, evitando que o conhecimento se perca ou que discussões antigas precisem ser repetidas.

## Quando criar um novo ADR?
Crie um ADR sempre que uma decisão com impacto arquitetural for tomada. Exemplos incluem:
- Escolha ou substituição de frameworks/bibliotecas principais.
- Definição de protocolos de comunicação.
- Mudanças na estratégia de deploy ou infraestrutura.
- Adoção de novos padrões de design ou mudanças estruturais relevantes no código.

## Processo de Criação
1. **Copie o template:** Copie o arquivo `0000-template.md` para um novo arquivo.
2. **Preencha os dados:** Documente o contexto, as alternativas, a decisão tomada e as consequências esperadas.
3. **Abra um Pull Request (PR):** Envie o novo ADR via PR para o repositório.
4. **Revisão:** A decisão deve ser revisada e aprovada por pelo menos um outro desenvolvedor da equipe.
5. **Merge:** Uma vez aprovado (status "Aceito"), o PR é mergeado na branch principal.

## Convenção de Nomenclatura
Os arquivos devem seguir o formato `NNNN-titulo-da-decisao.md`:
- **NNNN**: Um número sequencial de 4 dígitos com zero à esquerda (ex: `0001`, `0002`).
- **titulo-da-decisao**: O título da decisão em *kebab-case*, todo em letras minúsculas e sem acentos.
  - Exemplo correto: `0015-utilizacao-de-redis-para-cache.md`

## Alterando o status de um ADR existente
**Nunca edite a decisão ou o contexto de um ADR já "Aceito".**
Se uma decisão arquitetural mudar (por exemplo, migrando de um banco de dados para outro):
1. Crie um **novo** ADR documentando a nova decisão.
2. Altere o status do ADR antigo para "Substituído" (ou "Obsoleto") e adicione um link apontando para o novo ADR.
3. No novo ADR, mencione no Contexto que ele substitui a decisão do ADR antigo.
