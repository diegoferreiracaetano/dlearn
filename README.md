# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta, onde o Backend for Frontend (BFF) orquestra a experiência do usuário.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (BFF):** [http://localhost:8081/swagger](http://localhost:8081/swagger) - Documentação interativa das rotas SDUI.
- **API App Gateway:** `POST http://localhost:8081/v1/app` - Endpoint genérico para resoluções de tela e ações.
- **Componentes SDUI:** [Docs de Componentes](docs/sdui/components.md) - Guia de referência de componentes.

---

## 🔐 Segurança e Multi-Factor Authentication (MFA)

O projeto implementa um motor de desafios (**Challenge Engine**) centralizado para operações críticas, suportando múltiplos fatores de autenticação de forma transparente para as camadas de UI.

### 🧩 Challenge Engine (Shared Module)
Localizado no módulo `:shared`, o `ChallengeEngine` intercepta erros de rede (ex: `428 Precondition Required`) e orquestra a resolução do desafio antes de repetir a requisição original.

#### Fatores Suportados:
- **OTP (One-Time Password)**: Suporte para SMS e E-mail via rota dinâmica `verify_account` (SDUI).
- **Biometria**: Integração com Face ID e Touch ID (Biometric Prompt) para validação local e remota.

### 🔄 Fluxo de Desafio OTP:
1. **Tentativa de Operação**: O App envia uma requisição protegida (ex: troca de senha).
2. **Intercepção**: O `ChallengeInterceptor` captura o erro `428` e o `challengeToken`.
3. **Resolução**: O `OtpChallengeHandler` navega o usuário para a tela de verificação.
4. **Conclusão**: Após a inserção do código, o `challengeToken` é validado e a requisição original é repetida automaticamente com o novo header de autorização.

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

- **`:shared`**: Contratos, Enums, Modelos SDUI, Repositórios, `ChallengeEngine` e lógica de rede.
- **`:server`**: Ktor BFF, Orchestrators, UseCase e Gestão de Desafios (OTP/Biometria).
- **`:composeApp`**: UI Multiplatform (Android/iOS), Engine SDUI e Injeção de Dependência (Koin).

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test` (Roda testes em todos os módulos)
