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

### 🧩 Challenge Engine & Fatores Suportados
Localizado no módulo `:shared`, o `ChallengeEngine` intercepta erros de rede (ex: `428 Precondition Required`) e orquestra a resolução do desafio antes de repetir a requisição original.
- **OTP (One-Time Password)**: Suporte para SMS e E-mail via rota dinâmica `verify_account` (SDUI).
- **Biometria**: Integração com Face ID e Touch ID (Biometric Prompt) para validação local e remota.

### 🛡️ O Filtro de Interceptação (`ChallengeInterceptor`)
A mágica acontece no módulo `:shared` através de um plugin customizado do **Ktor HttpClient**. Este interceptor monitora todas as respostas da rede e automatiza a repetição:

```kotlin
// ChallengeInterceptor.kt (Simplificado)
scope.receivePipeline.intercept(HttpReceivePipeline.After) { response ->
    if (response.status.value == 428) { // Precondition Required
        val session = json.decodeFromString<ChallengeSession>(response.bodyAsText())
        
        // Dispara o motor de desafio que abre a UI e aguarda resolução
        val result = engine.resolve(session) 

        if (result is ChallengeResult.Success) {
            // Repete a requisição ORIGINAL injetando os tokens de validação
            val retryCall = scope.request {
                takeFrom(originalRequest)
                header("X-Challenge-Token", result.data["validatedToken"])
                header("X-Transaction-ID", session.transactionId)
            }
            proceedWith(retryCall)
        }
    }
}
```

### 🔄 Fluxo Fim-a-Fim de Desafio (Ex: Troca de Senha)
1. **Tentativa**: O usuário tenta alterar a senha. O App envia `POST /v1/auth/password/change`.
2. **Intercepção (428)**: O servidor identifica a necessidade de MFA e retorna `HTTP 428` com o `transactionId`. O `ChallengeInterceptor` captura o erro.
3. **Resolução de UI**: O `GlobalEventHandler` (no `:composeApp`) intercepta o evento `GlobalEvent.Challenge` disparado pelo engine e abre o diálogo de verificação (OTP).
4. **Conclusão**: Após a validação do código pelo usuário, o `challengeToken` é validado no servidor e o interceptor **repete automaticamente** a requisição original, que agora é aprovada.

### 🛠️ Implementação no Backend (DSL `challengePreference`)
Para proteger rotas no `:server`, utilizamos uma DSL que abstrai a complexidade do desafio:

```kotlin
challengePreference(ChallengeType.OTP_EMAIL) {
    post("/change-password") {
        val request = call.receive<ChangePasswordRequest>()
        // O token validado fica disponível via extensão da call
        orchestrator.changePassword(request, call.challengeToken).collect { 
            call.respond(HttpStatusCode.OK, it) 
        }
    }
}
```

---

## 📡 Global Events (Barramento de Eventos)

Utilizamos o `GlobalEventDispatcher` para manter o desacoplamento total entre Domain/Data e UI.

### Como usar:
Injete o `GlobalEventDispatcher` e dispare eventos de qualquer lugar (ViewModel, Repository, UseCase):

```kotlin
val eventDispatcher: GlobalEventDispatcher by inject()

// Exibir um Snackbar automático
eventDispatcher.tryEmit(GlobalEvent.Message("Sucesso!", GlobalEvent.MessageType.SUCCESS))

// Forçar uma navegação
eventDispatcher.tryEmit(GlobalEvent.Navigation(NavigationRoutes.HOME))
```

---

## 🚀 Arquitetura de Navegação Dinâmica (SDUI)

O DLearn utiliza um sistema de **Roteamento Genérico** que permite a criação de novas telas e fluxos sem a necessidade de novos deploys.

### 1. Gateway Genérico (`/v1/app`)
O App utiliza o `AppRepository` para enviar requisições ao gateway.
- **Request (JSON)**:
  ```json
  { "path": "watchlist", "params": { "movieId": "123" } }
  ```
- **Funcionamento**: O BFF recebe o `path` e o `AppOrchestrator` resolve qual `Screen` ou `Action` retornar.

### 2. Formato de Rotas e Deeplinks
As rotas seguem o padrão `app?path={path}&ref={slug}&params={csv}`. O parâmetro `ref` é promovido a query param de primeiro nível para facilitar cache e SEO.
- **Exemplo**: `app?path=faq&ref=privacy-policy`

### 3. Navegação via AppAction
O Backend controla o fluxo enviando objetos `AppAction.Navigation` com rotas parametrizadas:
```json
{
  "type": "navigation",
  "route": "app?path=favorite&params=category:movies"
}
```

### 4. FAQ e Conteúdo Dinâmico (HTML)
Implementamos uma engine de conteúdo estático via SDUI para telas de suporte e legal.
- **Rota**: `app?path=faq&ref=[slug]`
- **Componente**: `AppHtmlTextComponent` - Renderiza HTML básico (h1, b, a, etc) através do Design System.
- **Armazenamento**: Conteúdos centralizados no servidor (`faq_content.json`).

---

## 🏗️ Estrutura do Projeto

- **`:shared`**: Contratos, `GlobalEventDispatcher`, `ChallengeEngine`, `ChallengeInterceptor` e `NavigationRoutes`.
- **`:server`**: Ktor BFF, Orchestrators de SDUI e Gestão de Desafios via DSL.
- **`:composeApp`**: UI Multiplatform, `GlobalEventHandler` (Consumidor de eventos) e Engine SDUI.

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test`
