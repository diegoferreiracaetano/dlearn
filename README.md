# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta, onde o Backend for Frontend (BFF) orquestra a experiência do usuário.

---

## 🔐 Multi-Factor Authentication (MFA) & Challenge Engine

O DLearn possui um motor de desafios (**Challenge Engine**) genérico controlado pelo servidor. Ele permite interceptar operações protegidas e exigir verificações adicionais (OTP, Biometria, etc.) de forma desacoplada da UI.

### 🔄 Fluxo Fim-a-Fim (Exemplo: Troca de Senha)
1. **Solicitação**: O usuário tenta alterar a senha. O App envia `POST /v1/auth/password/change`.
2. **Desafio (428)**: O servidor identifica que a operação exige MFA e retorna `HTTP 428 Precondition Required` com um `transactionId` no header.
3. **Interceptação Global**: O `ChallengeInterceptor` (no módulo `:shared`) captura o erro e dispara um `GlobalEvent.Challenge`.
4. **Resolução de UI**: O `GlobalEventHandler` (no `:composeApp`) intercepta o evento e navega o usuário para a rota de desafio (ex: Diálogo de OTP).
5. **Finalização**: Após o usuário inserir o código, o `ChallengeRepository` resolve o desafio no servidor. O interceptor então **repete automaticamente** a requisição original de troca de senha, que agora é aprovada.

### 📡 Global Events (Barramento de Eventos)
Para manter o total desacoplamento entre as camadas de lógica (Domain/Data) e a UI, utilizamos o `GlobalEventDispatcher`.

#### Como usar (Qualquer lugar do projeto):
Se você estiver em um `ViewModel`, `UseCase` ou `Repository` e precisar disparar uma navegação, mensagem ou desafio:

```kotlin
// Injetar o dispatcher
val eventDispatcher: GlobalEventDispatcher by inject()

// Disparar uma mensagem (Snackbar automático)
eventDispatcher.tryEmit(GlobalEvent.Message("Operação realizada!", GlobalEvent.MessageType.SUCCESS))

// Disparar uma navegação forçada
eventDispatcher.tryEmit(GlobalEvent.Navigation(NavigationRoutes.HOME))
```

---

## 🛠️ Implementação no Backend (BFF)

Para proteger uma rota com MFA, siga este padrão no módulo `:server`:

### 1. No Controller/Routing
Utilize o `ChallengeService` para validar se a transação atual já foi resolvida:

```kotlin
post("/change") {
    val transactionId = call.request.header("X-Transaction-ID")
    val challengeStatus = challengeService.checkStatus(transactionId)

    if (challengeStatus != ChallengeStatus.RESOLVED) {
        // Lança o desafio 428 se não resolvido
        val session = challengeService.createSession(ChallengeType.OTP_EMAIL)
        call.respond(HttpStatusCode.PreconditionRequired, session)
        return@post
    }
    
    // Se chegou aqui, o desafio foi vencido! Prossegue com a lógica...
}
```

---

## 🚀 Arquitetura de Navegação Dinâmica (SDUI)

O DLearn utiliza um sistema de **Roteamento Genérico** que permite a criação de novas telas e fluxos sem a necessidade de novos deploys.

### Gateway Genérico (`/v1/app`)
O App utiliza o `AppRepository` para enviar requisições ao gateway:
- **Request**: `{ "path": "profile/edit", "params": { "userId": "1" } }`
- **Funcionamento**: O BFF recebe o `path` e o `AppOrchestrator` resolve qual `ScreenBuilder` deve montar a resposta SDUI.

---

## 🏗️ Estrutura do Projeto

- **`:shared`**: Contratos, `GlobalEventDispatcher`, `ChallengeEngine` e lógica de rede Ktor.
- **`:server`**: Ktor BFF, Orchestrators de SDUI e Gestão de Desafios.
- **`:composeApp`**: UI Multiplatform, `GlobalEventHandler` (Consumidor de eventos) e Engine SDUI.

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test`
