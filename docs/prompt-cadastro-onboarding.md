Implemente os requisitos do projeto:
Crie Model, Service, testes de service, Repository e Controller
Regras de negócio:
Use Spring Security

# Prompt de Implementação — Cadastro e Onboarding

Implemente o fluxo de **cadastro e onboarding** de atletas conforme as especificações abaixo.

---

## Regras de negócio

**RN1 — Dados de cadastro**
O atleta deve informar os seguintes campos no cadastro:
- Nome
- E-mail
- Senha
- Data de nascimento
- Peso
- Altura

**RN2 — E-mail único**
O e-mail não pode estar duplicado no sistema. Caso já exista um cadastro com o mesmo e-mail, o sistema deve rejeitar o cadastro e exibir mensagem de erro.

**RN3 — Força da senha**
A senha deve ter no mínimo 8 caracteres. Senhas abaixo desse limite devem ser rejeitadas com orientação ao usuário.

**RN4 — Nível de experiência**
O atleta deve selecionar seu nível de experiência durante o cadastro. O valor deve ser salvo no perfil, sem impacto no fluxo por enquanto. Valores aceitos (enum):
- `INICIANTE`
- `INTERMEDIARIO`
- `AVANCADO`

---

## Ator

**Visitante** — usuário não autenticado.

**Pré-condições:**
- Sistema disponível
- Banco de dados acessível

---

## Fluxo principal

1. Visitante acessa a tela de cadastro.
2. Informa nome, e-mail, senha, data de nascimento, peso e altura. *(RN1)*
3. Seleciona seu nível de experiência. *(RN4)*
4. Sistema valida todos os campos obrigatórios.
5. Sistema verifica se o e-mail já está cadastrado. *(RN2)*
6. Sistema salva o atleta, autentica a sessão e redireciona para o dashboard.

---

## Fluxos alternativos

**E-mail duplicado** *(RN2)*
Sistema rejeita o cadastro e exibe mensagem informando que o e-mail já está em uso.

**Senha fraca** *(RN3)*
Sistema rejeita e orienta o atleta a usar no mínimo 8 caracteres.

**Campo obrigatório vazio**
Sistema destaca o campo inválido e impede o avanço para a próxima etapa.

---

## Pós-condição

Atleta cadastrado, autenticado e redirecionado para o dashboard com perfil completo salvo.

---

## Observações de implementação

- Todos os campos de RN1 são **obrigatórios**.
- O campo `nivel_experiencia` deve ser persistido como enum no banco de dados com os valores `INICIANTE`, `INTERMEDIARIO` e `AVANCADO`.
- Não há nenhuma lógica de negócio associada ao `nivel_experiencia` neste momento — apenas coleta e persistência.
- Após o cadastro bem-sucedido, a sessão do atleta deve ser iniciada automaticamente (sem necessidade de login separado).
- Senhas devem ser armazenadas com hash seguro (ex: bcrypt).
