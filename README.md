# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** para renderização dinâmica de interfaces via **Backend for Frontend (BFF)**.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (Local):** [http://localhost:8081/swagger](http://localhost:8081/swagger)
- **API Home (Local):** [http://localhost:8081/v1/home](http://localhost:8081/v1/home)

---

## 🏗️ Estrutura do Projeto

O projeto é modularizado para maximizar o compartilhamento de código entre plataformas:

### 1. `shared` (KMP Module)
O coração do ecossistema, onde reside a lógica de negócio e os modelos de dados.
- **`commonMain`**: Contratos SDUI (`sealed interface Component`), repositórios (`HomeRepository`, `SDUIRepository`), use cases e entidades de domínio.
- **Koin 4.1.0**: Injeção de dependência compartilhada.
- **Kotlinx Serialization**: Gerenciamento de JSON polimórfico para componentes SDUI.
- **Ktor Client**: Consumo de APIs REST com suporte a cache.

### 2. `server` (Ktor BFF)
O Backend for Frontend (BFF) que dita a interface do usuário.
- **Server-Driven UI**: Constrói objetos `Screen` contendo listas de componentes dinâmicos.
- **Orquestração**: Integra múltiplas fontes de dados (TMDB API, Pokedex, etc).
- **Cache Inteligente**: `InMemoryCache` para dados de API (5 min) e headers de cache HTTP.
- **Swagger/OpenAPI**: Documentação interativa disponível via Swagger.

### 3. `composeApp` (UI Compartilhada)
A aplicação principal escrita em **Jetpack Compose Multiplatform**.
- **SDUI Engine**: Motor genérico (`RenderComponent`) que converte JSON do servidor em Composables reais.
- **Design System**: Componentes atômicos reutilizáveis (Carrosséis, Banners, Chips, TopBars).
- **Navigation**: Gerenciamento de rotas e estado de navegação via Compose Navigation.
- **Plataformas**: Suporta **Android**, **iOS (via Compose)** e **Desktop**.

### 4. `iosApp` (Swift Wrapper)
O entrypoint para a plataforma iOS.
- **SwiftUI**: Utiliza `UIViewControllerRepresentable` para carregar a `ComposeView` central.
- **Bridge**: Integração direta com o módulo `shared` para inicialização do Koin e ciclo de vida.

---

## 🧩 Componentes SDUI Suportados

O App renderiza dinamicamente:
- `BannerCarousel`: Carrosséis de destaque com suporte a vídeo/imagem.
- `Carousel`: Listas horizontais de conteúdo (ex: Top 10, Sugestões).
- `ChipGroup`: Filtros rápidos dinâmicos definidos pelo backend.
- `AppTopBar` & `BottomNavigation`: Barras de navegação controladas via BFF.
- `FullScreenBanner`: Banners de impacto total para lançamentos.

---

## 🚀 Como Executar

### Backend (BFF)
1. Configure a `THE_MOVIE_DB_API_KEY` em `local.properties`.
2. Execute: `./gradlew :server:run`
3. Verifique em: `http://localhost:8081/v1/home`

### Aplicativo (Android/iOS)
- **Android**: Execute o módulo `composeApp` no Android Studio.
- **iOS**: Abra o diretório `iosApp` no Xcode ou execute via Android Studio (plugin KMP).
- **Desktop**: Execute `./gradlew :composeApp:run`

---

## 📄 Documentação & GitHub Pages

A documentação detalhada é gerada via **MkDocs Material** e publicada automaticamente via GitHub Actions.
- **Workflow**: `.github/workflows/docs.yml`
- **Configuração**: `mkdocs.yml`
- **Conteúdo**: Diretório `/docs`
