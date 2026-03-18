# DLearn - Kotlin Multiplatform (KMP) & SDUI Ecosystem

Este projeto é um ecossistema completo desenvolvido em **Kotlin Multiplatform (KMP)** que implementa uma arquitetura de **Server-Driven UI (SDUI)** robusta, onde o Backend for Frontend (BFF) orquestra a experiência do usuário.

## 🔗 Links Úteis

- **Documentação Técnica (GitHub Pages):** [https://diegoferreiracaetano.github.io/dlearn](https://diegoferreiracaetano.github.io/dlearn)
- **Swagger UI (BFF):** [http://localhost:8081/swagger](http://localhost:8081/swagger) - Documentação interativa das rotas SDUI.
- **API App Gateway:** [http://localhost:8081/v1/app/{path}](http://localhost:8081/v1/app/{path}) - Endpoint genérico para resoluções de tela.

---

## 🚀 Arquitetura de Navegação Dinâmica (SDUI)

O DLearn utiliza um sistema de **Roteamento Genérico** que permite a criação de novas telas sem a necessidade de novos deploys nas lojas (App Store/Play Store).

### 1. Rota Genérica de App (`app/{path}`)
Toda rota que começa com `app/` no Mobile é interceptada pelo `sduiComposable`. 
- **Exemplo**: `navController.navigate("app/watchlist")`
- **Funcionamento**: O App extrai o `path` ("watchlist") e faz uma requisição para o BFF em `/v1/app/watchlist`. O servidor responde com um objeto `Screen` contendo os componentes a serem renderizados.

### 2. Navegação via AppAction (O Jeito Correto)
O Frontend não deve construir URLs manualmente. O Backend envia objetos `AppAction.Navigation`:
```json
{
  "type": "NAVIGATION",
  "route": "favorite",
  "params": { "category": "movies" }
}
```
O App utiliza a extensão `navController.navigateToRoute(NavigationRoutes.fromAction(action))` para processar essa intenção, transformando-a na rota técnica `app/favorite?params=category:movies`.

---

## 🛠️ Como Adicionar uma Nova Tela (Fluxo Completo)

Para criar um novo fluxo (ex: "Minha Lista", "Histórico", "Configurações"):

### Passo 1: Backend (Server)
1. **Defina o Layout**: Crie um `ScreenBuilder` (ex: `MyListScreenBuilder.kt`) que monte a árvore de `Component`.
2. **Orquestre os Dados**: Crie um `Orchestrator` para buscar os dados necessários (TMDB, DB, etc).
3. **Registre no Gateway**: No `AppOrchestrator.kt`, adicione o novo path ao mapeamento:
   ```kotlin
   "my_list" -> myListScreenBuilder.build(...)
   ```

### Passo 2: Swagger
Valide sua nova rota acessando `http://localhost:8081/swagger`. Teste o path `my_list` e verifique se o JSON de resposta contém os componentes esperados.

### Passo 3: Mobile (App)
**Nada!** Se os componentes usados (Carrosséis, Banners, Cards) já existem no Design System, o App renderizará a nova tela automaticamente assim que receber o comando de navegação para `app/my_list`.

---

## 🧩 Componentes e Ícones
O projeto utiliza um mapeamento rigoroso entre o Backend e o Design System:
- **Ícones**: Definidos no enum `AppIconType` (Shared). Use `icon.toIcon()` no Compose para obter o `ImageVector` correspondente.
- **Componentes**: Cada `Component` (JSON) possui um `Renderer` correspondente no Compose.

---

## 🏗️ Estrutura do Projeto

- **`:shared`**: Contratos, Enums, Modelos SDUI e lógica de rede.
- **`:server`**: Ktor BFF, Orchestrators e Screen Builders.
- **`:composeApp`**: UI Multiplatform, SDUI Engine e Injeção de Dependência (Koin).

---

## 🛠️ Comandos de Desenvolvimento

- **Rodar BFF**: `./gradlew :server:run`
- **Rodar Android**: `./gradlew :composeApp:installDebug`
- **Rodar Testes**: `./gradlew test` (Roda testes em todos os módulos)
