# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta, onde o Backend for Frontend (BFF) orquestra a experiência do usuário.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (BFF):** [http://localhost:8081/swagger](http://localhost:8081/swagger) - Documentação interativa das rotas SDUI.
- **API App Gateway:** `POST http://localhost:8081/v1/app` - Endpoint genérico para resoluções de tela e ações.

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

## 🛠️ Como Adicionar uma Nova Tela (Fluxo Completo)

Para criar um novo fluxo (ex: "Minha Lista", "Histórico"):

### Passo 1: Backend (Server)
1. **Defina o Layout**: Crie um `ScreenBuilder` (ex: `MyListScreenBuilder.kt`) que monte a árvore de `Component`.
2. **Orquestre os Dados**: Crie um `Orchestrator` para buscar os dados necessários.
3. **Registre no Gateway**: No `AppOrchestrator.kt`, adicione o novo path ao mapeamento `when (request.path)`.

### Passo 2: Swagger
Valide sua nova rota acessando `http://localhost:8081/swagger`. Teste o payload JSON e verifique a resposta.

### Passo 3: Mobile (App)
**Automático!** Se os componentes usados já existem no Design System, o App renderizará a nova tela assim que receber a resposta do servidor.

---

## 🧩 Componentes e Design System
O projeto utiliza um mapeamento rigoroso entre o Backend e o Design System:
- **Componentes**: Cada `Component` (JSON) possui um `Renderer` correspondente no Compose.
- **Ícones**: Definidos no enum `AppIconType`.
- **Tematização**: Cores e dimensões são controladas via tokens no Design System.

---

## 🏗️ Estrutura do Projeto

- **`:shared`**: Contratos, Enums, Modelos SDUI, Repositórios e lógica de rede.
- **`:server`**: Ktor BFF, Orchestrators, Screen Builders e Integração TMDB.
- **`:composeApp`**: UI Multiplatform (Android/iOS), Engine SDUI e Injeção de Dependência (Koin).

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test` (Roda testes em todos os módulos)
