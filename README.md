# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** para renderização dinâmica de interfaces via **Backend for Frontend (BFF)**.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (Local):** [http://localhost:8081/swagger](http://localhost:8081/swagger)
- **API App Gateway (Local):** [http://localhost:8081/v1/app](http://localhost:8081/v1/app)
- **API Home (Local):** [http://localhost:8081/v1/home](http://localhost:8081/v1/home)

---

## 🏗️ Estrutura do Projeto

O projeto é modularizado para maximizar o compartilhamento de código entre plataformas:

### 1. `shared` (KMP Module)
O coração do ecossistema, onde reside a lógica de negócio e os modelos de dados.
- **`commonMain`**: Contratos SDUI (`sealed interface Component`), repositórios, use cases e entidades de domínio.
- **Koin 4.1.0**: Injeção de dependência compartilhada.
- **Kotlinx Serialization**: Gerenciamento de JSON polimórfico para componentes SDUI.
- **Ktor Client**: Consumo de APIs REST com suporte a cache.

### 2. `server` (Ktor BFF)
O Backend for Frontend (BFF) que dita a interface do usuário.
- **SDUI Gateway (`/v1/app`)**: Ponto central genérico para fluxos dinâmicos.
- **Orquestração**: Integra múltiplas fontes de dados (TMDB API, Pokedex, etc).
- **Cache Inteligente**: `InMemoryCache` para dados de API (5 min) e headers de cache HTTP.

### 3. `composeApp` (UI Compartilhada)
A aplicação principal escrita em **Jetpack Compose Multiplatform**.
- **SDUI Engine**: Motor genérico que converte JSON do servidor em Composables reais.
- **Generic App Flow**: Uso de `AppScreen` e `AppViewModel` para renderizar qualquer rota definida pelo backend via Gateway.
- **Plataformas**: Suporta **Android**, **iOS (via Compose)** e **Desktop**.

---

## 🚀 Fluxos de UI Dinâmicos (SDUI)

O projeto suporta dois tipos principais de fluxos de tela:

### A. Fluxo Implícito (Específico)
Usado quando a tela possui componentes ou comportamentos nativos muito complexos que exigem uma "casca" fixa no app (ex: `Home`, `Profile`).
1. O App navega para uma rota fixa.
2. O `AppMainContentRenderer` identifica a rota e chama um Composable específico (ex: `HomeScreen`).
3. O Composable específico gerencia seu próprio ViewModel e estado.

### B. Fluxo Genérico (Totalmente Dinâmico) - **RECOMENDADO**
Usado para novos fluxos (ex: `Watchlist`, `Favorites`, `History`) onde o app não tem conhecimento prévio da tela.
1. O App navega para uma rota (ex: `watchlist`).
2. O `AppMainContentRenderer` não reconhece a rota como "implícita" e delega para o `AppScreen`.
3. O `AppScreen` (genérico) solicita ao Gateway (`/v1/app`) o conteúdo para aquele `path`.
4. O Backend retorna um objeto `Screen` completo com `Components` e `Actions`.
5. O App renderiza tudo dinamicamente.

#### Como criar um novo Fluxo Genérico:
1. **No Shared**: Adicione a nova rota em `NavigationRoutes.kt`.
2. **No Backend**:
   - Crie um `ScreenBuilder` para a nova tela.
   - Crie um `Orchestrator` para a lógica de dados.
   - Registre a rota no `AppOrchestrator.kt`.
3. **No App**: Não é necessário mexer em nada! O `AppMainContentRenderer` já cuidará da renderização.

---

## 🧩 Componentes SDUI Suportados

O App renderiza dinamicamente:
- `AppSearchBar`: Barra de busca controlada pelo servidor.
- `AppEmptyState`: Feedback visual customizado.
- `BannerCarousel`: Carrosséis de destaque.
- `Carousel`: Listas horizontais de conteúdo.
- `AppContainer`: Container raiz que define TopBar e BottomBar dinamicamente.

---

## 🛠️ Comandos Úteis

- **Backend**: `./gradlew :server:run`
- **Testes**: `./gradlew test`
- **Swagger**: Acesse `http://localhost:8081/swagger` após rodar o server.
