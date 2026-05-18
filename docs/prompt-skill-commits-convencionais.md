# Prompt - Skill de Commits Convencionais

Na skill, use o padrão de criação de skills do Codex que aparece a descrição quando vou usar

Adicione uma skill para fazer commits seguindo Conventional Commits, em portugues-BR, sendo conciso e especifico ao mesmo tempo.

## Diretrizes

- Priorize commits parciais por escopo logico.
- Separe commits por tipo de alteracao e camada, como service, repository, migrations, controller, model, DTO, testes e configuracoes.
- Use mensagens em portugues-BR, objetivas e especificas sobre o que mudou.
- Siga o formato `tipo(escopo): descricao`.
- Use tipos convencionais como `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`, `build` e `ci`.
- Evite commits genericos como `ajustes`, `alteracoes` ou `wip`.
- Antes de commitar, revise `git status` e `git diff` para garantir que cada commit tenha uma responsabilidade clara.

## Exemplos

```text
feat(service): adiciona regra de cadastro de atleta
feat(repository): cria consulta por email do atleta
feat(controller): expõe endpoint de cadastro
test(service): cobre validacao de email duplicado
chore(migration): cria tabela de atletas
```
