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
Para proteger rotas no `:server`, utilizamos uma DSL que abraça a complexidade do desafio:

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

## 📦 Resiliência e Cache (Offline & Performance)

O DLearn implementa um sistema de cache inteligente que atua tanto na resiliência do App (Offline Mode) quanto na performance do Backend (BFF).

### 🧩 Estrutura de Cache (:shared)
O módulo `:shared` define a abstração `CacheManager`, permitindo implementações específicas para cada ambiente:
- **`PersistentCacheManager` (App)**: Persiste objetos `Screen` e dados no disco usando `KeyValueStorage`. Garante que o usuário veja conteúdo mesmo em modo avião.
- **`InMemoryCacheManager` (Server)**: Mantém telas pré-montadas na memória RAM do servidor para evitar reprocessamento de lógica pesada.

### 🚀 A Extensão `.toCache()`
Qualquer `Flow` de dados pode se tornar resiliente apenas adicionando a extensão `.toCache()`.

```kotlin
// Exemplo no AppRepository
override fun execute(path: String, params: Map<String, String>?): Flow<Screen> {
    return flow {
        val response = httpClient.post("/v1/app") { 
            setBody(AppRequest(path, params))
        }.body<Screen>()
        emit(response)
    }.toCache(key = "sdui_${path}_${params?.hashCode()}")
}
```

### ⚙️ Estratégias de Cache
1. **`NETWORK_FIRST` (Padrão App)**: Prioriza a rede. Se houver falha (ex: `IOException`), recupera automaticamente do cache local e emite o dado, suprimindo o erro para a UI.
2. **`CACHE_FIRST` (Padrão Server)**: Verifica o cache antes de qualquer processamento. Se o dado existir, retorna imediatamente, economizando CPU.

---

## 🎨 Design System (Regra Crítica)

O projeto segue uma política rigorosa de **Zero Hardcode**. Toda a UI é montada a partir de componentes atômicos definidos no `:shared`.

- **Componentes Atômicos**: Botões, inputs e cards são definidos como modelos no `:shared` e renderizados nativamente no App.
- **Tokens de Tema**: Cores, espaçamentos e tipografia são consumidos de um `ThemeContext` centralizado.
- **Extensibilidade**: Se um componente não existe no Design System, ele deve ser criado no `:shared` antes de ser usado no backend.

---

## 👤 Autenticação e Cadastro (Fluxo Híbrido)

Embora o projeto utilize SDUI extensivamente, os fluxos de **Login e Cadastro** são implementados de forma híbrida para garantir performance e uma experiência de entrada fluida.

### 📝 Cadastro de Usuário (Manual UI)
Diferente das demais telas, o `SignUpScreen` é implementado nativamente no app Android/iOS utilizando os componentes do Design System.
- **ViewModel Compartilhada**: O `SignUpViewModel` reside no módulo `:shared` e gerencia o estado da UI (`SignUpUIState`).
- **Persistência de Sessão**: Após o cadastro bem-sucedido, o `SessionManager` armazena os tokens JWT e navega automaticamente para a Home.
- **Validação de Erros**: O backend retorna mensagens de erro localizadas (ex: "E-mail já cadastrado") que são exibidas diretamente nos campos do formulário.

### 🔑 Autenticação (Login)
O login segue um padrão similar, mas está preparado para disparar o **Challenge Engine** (MFA) caso o servidor detecte um login em novo dispositivo ou atividade suspeita.

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

### 5. Configurações e Persistência Híbrida (SDUI + Local)
Implementamos telas de configurações que utilizam o poder do SDUI para interface, mas mantêm a persistência local via `AppPreferences` (KMP Settings).
- **Sincronização**: O `SettingsViewModel` observa mudanças nas preferências locais e dispara um `retry()` automático na requisição SDUI, garantindo que o servidor sempre retorne os textos e estados traduzidos e atualizados.
- **Clear Cache**: Ação `"clear_cache"` dispara um diálogo de confirmação local antes de executar o logout e limpeza de dados.

---

## 🏗️ Estrutura do Projeto (Clean Architecture)

- **`:shared`**: O "Cérebro". Contratos, `GlobalEventDispatcher`, `ChallengeEngine`, `ChallengeInterceptor`, `AppPreferences` (KMP Settings) e `NavigationRoutes`.
- **`:server`**: O "Orchestrator". Ktor BFF, Orchestrators de SDUI, Mappers e Builders de telas dinâmicas (`ScreenBuilder`).
- **`:composeApp`**: A "View". UI Multiplatform, `GlobalEventHandler` (Consumidor de eventos), implementação do Design System e Factory de renderização SDUI.

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test`
