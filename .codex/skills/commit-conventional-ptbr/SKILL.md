---
name: commit-conventional-ptbr
description: Use when the user needs git commit messages or commit planning with Conventional Commits in pt-BR, especially to split work by partial scope across service, repository, migrations, controller, tests, and related layers.
---

# Commits Convencionais em pt-BR

Use esta skill para planejar commits pequenos, parciais e consistentes com Conventional Commits.

## Fluxo

1. Identifique o menor escopo coerente.
2. Separe alterações por camada: `migration`, `repository`, `service`, `controller`, `model`, `dto`, `test`, `config`, `docs`.
3. Prefira um commit por responsabilidade isolada.
4. Quando a mudança atravessar camadas, quebre em commits na ordem natural de dependência: migrations, repository, service, controller, tests.
5. Escreva a mensagem no formato `tipo(escopo): descricao`, em pt-BR, curta e específica.

## Tipos

- `feat`
- `fix`
- `refactor`
- `test`
- `docs`
- `chore`
- `build`
- `ci`
- `style`

## Regras

- Priorize commits parciais.
- Evite mensagens genéricas como `wip`, `ajustes`, `alteracoes` ou `misc`.
- Use escopos concretos e fáceis de ler.
- Se um commit ficar grande, volte e separe por arquivo, camada ou regra de negócio.
- Prefira manter cada commit compreensível sozinho.

## Exemplos

```text
feat(service): adiciona validação de email duplicado
feat(repository): cria busca de atleta por email
feat(controller): expõe endpoint de cadastro
test(service): cobre regra de senha forte
chore(migration): cria tabela de atletas
```
