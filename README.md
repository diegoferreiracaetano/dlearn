# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta, onde o Backend for Frontend (BFF) orquestra a experiência do usuário.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (BFF):** [http://localhost:8081/swagger](http://localhost:8081/swagger) - Documentação interativa das rotas SDUI.
- **API App Gateway:** `POST http://localhost:8081/v1/app` - Endpoint genérico para resoluções de tela e ações.
- **Componentes SDUI:** [Docs de Componentes](docs/sdui/components.md) - Guia de referência de componentes.

---

## 🔐 Fluxo de Segurança: Alteração de Senha com Desafio OTP

O projeto implementa um fluxo de segurança de dois passos (Two-Step Challenge) para operações críticas, como a troca de senha.

### Passo a Passo do Fluxo:

1. **Tentativa de Troca (`POST /v1/password/change`)**:
   - O App envia a nova senha.
   - O servidor retorna `428 Precondition Required` com um `challengeToken`.
   - **Resultado:** A senha NÃO é alterada neste momento.

2. **Desafio OTP (Tela de Verificação)**:
   - O App captura o erro `428` e abre a tela de OTP (`VerifyAccountScreen`).
   - O usuário insere o código (Ex: `123456` ou `000000` para Debug).

3. **Verificação do Código (`POST /v1/password/verify-otp`)**:
   - O App envia o código e o `challengeToken`.
   - O servidor valida e retorna um `validatedToken`.
   - **Mensagem:** `PASSWORD_OTP_VERIFIED` (Indica que o código está correto, mas a senha ainda não mudou).

4. **Conclusão da Troca (`POST /v1/password/change`)**:
   - O App reenvia a troca de senha incluindo o header `X-Challenge-Token: <validatedToken>`.
   - O servidor valida o token e efetiva a alteração da senha.
   - **Resposta:** `200 OK` com `PASSWORD_CHANGE_SUCCESS`.

---

## 🚀 Arquitetura de Navegação Dinâmica (SDUI)

O DLearn utiliza um sistema de **Roteamento Genérico** que permite a criação de novas telas e fluxos sem a necessidade de novos deploys nas lojas (App Store/Play Store).

### 1. Gateway Genérico (`/v1/app`)
O App utiliza o `AppRepository` para enviar requisições ao gateway.
- **Request (JSON)**:
  ```json
  {
    "path": "watchlist",
    "params": { "movieId": "123" }
  }
  ```
- **Funcionamento**: O BFF recebe o `path` e delega para o `AppOrchestrator`, que resolve qual `Screen` ou `Action` retornar.

### 2. Navegação via AppAction
O Backend controla o fluxo enviando objetos `AppAction.Navigation`:
```json
{
  "type": "navigation",
  "route": "app/favorite",
  "params": { "category": "movies" }
}
```
O App processa essa intenção, transformando-a em uma chamada ao gateway ou uma navegação interna.

---

## 🏗️ Estrutura do Projeto

- **`:shared`**: Contratos, Enums, Modelos SDUI, Repositórios e lógica de rede.
- **`:server`**: Ktor BFF, Orchestrators, UseCase e Gestão de Desafios (OTP).
- **`:composeApp`**: UI Multiplatform (Android/iOS), Engine SDUI e Injeção de Dependência (Koin).

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test` (Roda testes em todos os módulos)
