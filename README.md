# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

O **DLearn** é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta. O diferencial deste projeto é o controle total da experiência do usuário através do backend, utilizando um sistema de navegação baseado em strings dinâmicas.

---

## 🔗 O Coração do Projeto: Navegação via String SDUI

A inteligência do DLearn reside na forma como o App e o Backend se comunicam. Em vez de endpoints fixos para cada tela, utilizamos uma **String de Navegação** que define o contrato de UI.

### O Padrão de Rota
Toda ação no aplicativo (cliques em botões, banners, notificações) é disparada por uma string no formato:
`app?path={path}&params={key1}:{value1},{key2}:{value2}`

- **`path`**: O identificador da tela ou fluxo no backend (ex: `home`, `movie/detail`, `watchlist`).
- **`params`**: Um mapa de parâmetros contextuais passados em formato CSV (ex: `movieId:550`).

### Ciclo de Vida da Requisição
1. **Interceptação**: O `ScreenRouter` no App captura a string.
2. **Contrato AppRequest**: O App envia um POST para o gateway `/v1/app` com o `path` e os `params`.
3. **Orquestração**: O Backend (`:server`) processa o path, busca os dados necessários e constrói uma `Screen`.
4. **Renderização**: O App recebe uma lista de `Component` polimórficos e o motor de renderização nativo reconstrói a UI.

---

## 🛡️ Segurança e Multi-Factor Authentication (MFA)

O DLearn possui um motor de desafios (**Challenge Engine**) centralizado para proteger operações sensíveis.

- **Detecção Automática**: O servidor retorna `428 Precondition Required` quando um desafio é necessário.
- **Intercepção de Rede**: O `ChallengeInterceptor` captura esse erro, suspende a requisição original e abre a UI de desafio (OTP, Biometria, etc).
- **Resiliência**: Após a resolução, a requisição original é **repetida automaticamente** com os tokens de validação.

> [Consulte os detalhes técnicos do MFA na documentação do Servidor](docs/server/index.md#--challenge-engine-mfa-dinâmico).

---

## 🏗️ Documentação do Ecossistema

O projeto é modularizado para garantir a máxima reutilização de código:

- **[Módulo Shared (KMP)](docs/kmp/index.md)**: Contratos de componentes, lógica de rede e injeção de dependência.
- **[Módulo Server (BFF)](docs/server/index.md)**: Orquestradores de tela, Builders de componentes e segurança.
- **[Módulo ComposeApp](docs/mobile/index.md)**: Implementação do Design System e Motor de Renderização nativo.
- **[Catálogo de Componentes](docs/sdui/components.md)**: Referência visual e técnica de todos os componentes disponíveis.
- **[Swagger UI (Interativo)](http://localhost:8081/swagger)**: Teste as rotas do BFF em tempo real.

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar Backend**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Executar Testes**: `./gradlew test`
