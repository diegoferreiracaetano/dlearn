# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

O **DLearn** é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta. O diferencial deste projeto é o controle total da experiência do usuário através do backend, utilizando um sistema de navegação baseado em strings dinâmicas e um motor de renderização nativo e performático.

---

## 📱 Demonstração do App

Confira o ecossistema em ação, demonstrando a fluidez da UI gerada pelo servidor e a paridade entre plataformas.

| Vídeo de Demonstração | Interface & SDUI |
|:---:|:---:|
| <video src="docs/sample/app.mp4" width="300" controls>Seu navegador não suporta a tag de vídeo.</video> | <img src="https://raw.githubusercontent.com/diegoferreiracaetano/dlearn/main/docs/assets/demo.gif" width="300" alt="DLearn Demo GIF"> |
| *Demonstração completa do fluxo* | *Preview rápido de componentes* |

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
3. **Orquestração**: O Backend (`:server`) processa o path e constrói uma `Screen`.
4. **Renderização**: O App recebe a lista de `Component` e reconstrói a UI dinamicamente.

---

## 🛡️ Segurança e Multi-Factor Authentication (MFA)

O DLearn possui um motor de desafios (**Challenge Engine**) centralizado para proteger operações sensíveis.
- **Intercepção de Rede**: O `ChallengeInterceptor` captura erros `428 Precondition Required`.
- **Resiliência**: Após a resolução do desafio (OTP/Biometria), a requisição original é repetida automaticamente.

---

## 🌍 Internacionalização (I18n)
O backend suporta múltiplos idiomas (PT-BR, EN-US, ES-ES) com suporte nativo a **UTF-8**, garantindo que caracteres especiais e acentuações sejam renderizados corretamente em todas as plataformas.

---

## 🏗️ Documentação do Ecossistema

- **[Módulo Shared (KMP)](docs/kmp/index.md)**: Contratos de componentes e lógica de rede.
- **[Módulo Server (BFF)](docs/server/index.md)**: Orquestradores de tela, Builders de componentes e segurança.
- **[Módulo ComposeApp](docs/mobile/index.md)**: Design System e Motor de Renderização nativo.
- **[Swagger UI (Interativo)](http://localhost:8081/swagger)**: Teste as rotas do BFF em tempo real.

---

## ⚙️ CI/CD Pipeline
Possuímos um fluxo de integração contínua automatizado via GitHub Actions que garante a qualidade do código:
- **Lint**: Verificação estática com Detekt.
- **Testes Unitários**: Cobertura total nos módulos Server, Shared e App.
- **Testes Instrumentados**: Validação de UI em emuladores Android.
- **Relatórios**: Geração automática de relatórios de cobertura (Kover/Jacoco).

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar Backend**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Executar Todos os Testes**: `./gradlew test`
- **Executar Lint**: `./gradlew detektAll`
