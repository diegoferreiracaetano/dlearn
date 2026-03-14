# Contexto e Regras do Projeto (KMP)

Deve-se seguir estas diretrizes rigorosamente. Se não for possível cumpri-las, peça esclarecimentos.

## 1. Estrutura e Arquitetura
- Projeto **Kotlin Multiplatform (KMP)**.
- Respeitar a estrutura de pastas atual (`commonMain`, `androidMain`, etc.).
- Antes da alteração explique o plano de implementação.
- Aplicar **Clean Code** e SOLID.
- Não deixe string, texto, cores e temas hardcode.

## 2. Design System (REGRA CRÍTICA)
- Usar **APENAS** componentes do Design System do projeto.
- **BLOQUEIO:** Se um componente necessário não existir, **PARAR A GERAÇÃO IMEDIATAMENTE**.
- Avisar: "Componente [nome] não encontrado no Design System" e sugerir a criação do componente antes de prosseguir.
- **Zero Hardcode:** Proibidas strings, cores ou dimensões fixas. Usar tokens de tema e recursos (ex: MOKO ou BuildKonfig).

## 3. Gerenciamento de Estado (State Management)
- **AppContainerState:** Use o `LocalAppContainerState.current` para gerenciar estados globais de `isLoading`, `error` e `onRetry`.
- **Sincronização:** Utilize `LaunchedEffect(uiState)` dentro das Screens para sincronizar o estado da ViewModel com o `AppContainerState`.
- **Composição:** O conteúdo da tela (`Success`) deve permanecer na composição sempre que possível. O `AppContainerRenderer` cuidará das sobreposições (overlays) de carregamento e erro.
- **Retry:** Implemente o `onRetry` no `ComponentActions` e sincronize-o com o `AppContainerState` para fornecer feedback imediato ao usuário.

## 4. Backend e Front
- Siga a mesma arquitetura já existe no projeto.
- Use sempre a estrutura de Component onde ele gera um ScreenBuilder para cada tela.
- No Front, consuma e monte as telas dinamicamente.
- No Front, nunca crie componentes que não constam no backend. Se necessário, sugira a criação do componente antes de prosseguir.

## 5. Testes e Documentação
- Gerar sempre testes unitários no `commonTest`.
- Atualizar o `README.md` para novas funcionalidades.
- Manter as especificações **OpenAPI / Swagger** sincronizadas com mudanças em modelos ou redes.
