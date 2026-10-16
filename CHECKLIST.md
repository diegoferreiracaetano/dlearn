# CHECKLIST — UDF nos ViewModels (Action / State / Effect)

Objetivo: aplicar fluxo de dados unidirecional de verdade nos ViewModels de
`shared/src/commonMain/.../ui/viewmodel`. O estado desce, a ação sobe, o efeito
de uma vez só tem canal próprio.

Método: **um item por vez**. Explicar antes, mostrar o código, testar, e só
então marcar. Nada de código antes do item estar descrito aqui.

## Diagnóstico (2026-10-16)

- [ ] D1. Não existe contrato de ações; cada ViewModel expõe métodos soltos.
- [ ] D2. Nomes diferentes para a mesma coisa: `fetch`, `execute`, `action`,
      `loadContent`, `loadMain`, `retry`.
- [ ] D3. `AppViewModel.action(String)` é só um `fetch` por URL com outro nome.
- [ ] D4. Dois `fetch` no `AppViewModel`: só `fetch(path, params)` põe `Loading`.
- [ ] D5. `MovieDetailViewModel.execute(url)` não põe `Loading`, não guarda o
      último pedido (o `retry` repete o antigo) e não cancela a busca anterior.
- [ ] D6. `LoginUIState.Success(isLoggedIn)` é gatilho de navegação dentro do
      estado; `signInWith` escreve o estado em dois pontos (corrida).
- [ ] D7. A UI decide quando carregar (`checkSession`, `fetch` na entrada da
      tela) em vez do ViewModel.
- [ ] D8. `Error(throwable)` leva exceção crua para a UI.

## Padrão alvo

- `sealed interface <Tela>Action` por tela, com um único `onAction(action)`.
- `UiState` só de leitura em `StateFlow`.
- `<Tela>Effect` num canal próprio, só para o que é daquela tela
  (navegação, mensagem). `GlobalEvent` fica para o que atravessa o app (MFA,
  erro de rede).
- Erro como `UiError` (mensagem, tipo, pode tentar de novo), não `Throwable`.
- Um único caminho de carregamento: sempre `Loading`, guarda o último
  `AppRequest`, cancela o job anterior.

## Restrição do projeto (AGENTS.md, seção 4)

`AppScreen` e `AppViewModel` são infraestrutura SDUI genérica: **não** criar
variantes (`HomeAppViewModel`...) nem mudar assinatura para injetar
repositórios. Toda comunicação continua via `AppRequest` (`path`, `params`,
`metadata`). A mudança no `AppViewModel` é só no contrato de entrada
(`onAction`) e no carregamento interno.

## M1 — Tipos base

- [x] M1.1 Reusar `AppError` (já existe em `domain/error`) em vez de criar
      `UiError`: `Throwable.toAppError()` movido para o `shared`
      (`AppErrorMapping.kt`) e `AppError.canRetry` derivado do código.
      Testes em `AppErrorMappingTest` (`commonTest`). Os estados passam de
      `Error(throwable)` para `Error(error: AppError)` ViewModel por ViewModel,
      nos marcos M2–M4.
      Pendente de verificar: `composeApp` não foi compilado aqui (precisa do
      `designsystem 1.0.34` do GitHub Packages, 401 sem credencial).
- [ ] M1.2 Canal de efeitos reutilizável por ViewModel (sem perder evento
      quando a tela não está coletando).
- [ ] M1.3 Carregamento padrão: `Loading` sempre, guarda último pedido,
      cancela job anterior.
- [ ] M1.4 Decidir o destino de `UIState<T>` (separar primeira carga de
      recarga com conteúdo antigo na tela).
- [ ] M1.5 Testes dos tipos base (`commonTest`).
- [ ] Fechamento: build, testes, revisão do checklist.

## M2 — Login (piloto)

- [ ] M2.1 `LoginAction` (`Submit`, `SocialSignIn`, `CheckSession`) e `onAction`.
- [ ] M2.2 `LoginUIState` sem `Success(isLoggedIn)`; `Error` com `UiError`.
- [ ] M2.3 `LoginEffect` (`NavigateToHome`) no lugar do `Success`.
- [ ] M2.4 `checkSession` no `init` do ViewModel; um só ponto de escrita do
      estado em `signInWith`.
- [ ] M2.5 `LoginScreen`/`WelcomeScreen` só enviam ações e coletam efeito.
- [ ] M2.6 Testes do ViewModel e das telas.
- [ ] Fechamento: build, testes, revisão do checklist.

## M3 — AppViewModel e MovieDetailViewModel

- [ ] M3.1 `AppAction` (`Load`, `Retry`, `Navigate`) substituindo `fetch`,
      `action` e `retry` soltos; um único caminho interno.
- [ ] M3.2 `MovieDetailViewModel` usa o mesmo caminho (corrige D5).
- [ ] M3.3 `retry` repete o último pedido de verdade.
- [ ] M3.4 Revisar chamadores em `AppScreen`, `MainScreen` e Settings.
- [ ] M3.5 Testes.
- [ ] Fechamento: build, testes, revisão do checklist.

## M4 — Demais ViewModels

- [ ] M4.1 `SignUp`
- [ ] M4.2 `VerifyAccount`
- [ ] M4.3 `CreateNewPassword`
- [ ] M4.4 `Logout` e `CleanCache`
- [ ] M4.5 `Settings` e `Main`
- [ ] Fechamento por ViewModel: build, testes, revisão do checklist.

## M5 — Testes

- [ ] M5.1 Trocar `verify { viewModel.x() }` por asserção de comportamento
      (sequência de estados + efeito), com Turbine.
- [ ] M5.2 Atualizar `README.md` e a documentação em `docs/`.
- [ ] Fechamento final: build, testes (`commonTest` e instrumentados),
      revisão do checklist.

## Ambiente de teste

- Rodar o Gradle com JDK 21 (`JAVA_HOME` do Temurin 21); o JDK 26 padrão do
  shell quebra o build.
- `./gradlew :shared:jvmTest` funciona; o `composeApp` precisa do
  `designsystem 1.0.34` do GitHub Packages (credencial `gpr.user`/`gpr.key`).
- Falhas que já existiam antes deste trabalho: `MainViewModelTest`
  ("when retry is called should reload main content" e "when configuration
  changes should reload main content", `Flow is empty`).

## Fora de escopo por enquanto

- Mudar o contrato do backend SDUI.
- Tela de pagamento e demais fluxos que ainda não existem aqui.
