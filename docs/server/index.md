# Backend for Frontend (BFF) com Ktor

O servidor do DLearn funciona como o cérebro do ecossistema SDUI. Ele é responsável por orquestrar a lógica de negócio, segurança e a composição da interface do usuário.

---

## 🏗️ Clean Architecture no BFF

Seguimos uma arquitetura limpa adaptada para SDUI:

1.  **Controllers**: Recebem `AppRequest` (POST `/v1/app`) e chamam o `AppOrchestrator`.
2.  **AppOrchestrator**: Gateway central que roteia o `path` para o `ScreenBuilder` correto.
3.  **ScreenBuilders**: Onde a "mágica" acontece. Eles decidem quais componentes compõem a tela baseados nos `params` do request.
4.  **Mappers**: Convertem modelos de domínio (ex: `Movie`) em componentes de UI (`MovieItemComponent`).
5.  **Challenge Engine**: DSL para gerenciar fluxos de MFA de forma declarativa.

---

## 🛡️ Challenge Engine (MFA Dinâmico)

O sistema de segurança utiliza o código HTTP `428 Precondition Required` para interceptar ações críticas e exigir fatores extras de autenticação.

### Fluxo de Segurança
1.  **Intercepção**: O servidor detecta que a ação (ex: `/v1/password/change`) exige MFA.
2.  **Resposta 428**: O BFF retorna erro 428 com os detalhes do desafio (`ChallengeType`).
3.  **Resolução Automática**: O `ChallengeInterceptor` no App captura o erro, resolve o desafio via OTP/Biometria e **repete a chamada original**.
4.  **Sucesso**: A chamada repetida agora é autorizada com o token de validação.

### Como Implementar no Backend
Adicione a verificação no Orchestrator ou Service:
```kotlin
if (!hasValidatedMfa(userId)) {
    throw ChallengeException(ChallengeType.OTP_EMAIL)
}
```

---

## 📡 Documentação Interativa (Swagger)

O projeto mantém uma especificação OpenAPI 3.0 completa para facilitar o desenvolvimento.

-   **Endpoint**: [http://localhost:8081/swagger](http://localhost:8081/swagger)
-   **Arquivo**: `server/src/main/resources/documentation.yaml`

### Principais Endpoints
-   **`POST /v1/app`**: Gateway genérico SDUI.
-   **`POST /v1/auth/login`**: Autenticação nativa.
-   **`POST /v1/auth/challenge/resolve`**: Resolução de MFA.

---

## 🚀 Como Rodar o Servidor
Execute o comando abaixo na raiz do projeto:
```bash
./gradlew :server:run
```
O servidor estará disponível em `http://0.0.0.0:8081`.
