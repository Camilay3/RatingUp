# 0005 - Nginx como Reverse Proxy

## Data
2026-09-25

## Status
Aceito

## Contexto
Para integrar o frontend (Angular), o backend (Spring Boot) e os serviços de armazenamento sob um mesmo domínio, evitando problemas de CORS ou a necessidade de expor várias portas publicamente, era necessário um web server de borda. (Contexto inferido a partir do código atual).

## Decisão
Adotamos o Nginx como proxy reverso atuando no roteamento do tráfego para os contêineres corretos (frontend, backend API, Minio).

## Consequências
Benefícios: Centralização do tráfego, melhoria de segurança não expondo a API diretamente, facilidade para implementar HTTPS no futuro.
Desvantagens: Necessidade de manter arquivos de configuração (.conf) adicionais e entender as regras de roteamento.
