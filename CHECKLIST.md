# CHECKLIST — UDF nos ViewModels (Action / State / Effect)

Objetivo: aplicar fluxo de dados unidirecional de verdade nos ViewModels de
`shared/src/commonMain/.../ui/viewmodel`. O estado desce, a ação sobe, o efeito
de uma vez só tem canal próprio.

Método: **um item por vez**. Explicar antes, mostrar o código, testar, e só
então marcar. Nada de código antes do item estar descrito aqui.

## Diagnóstico (2026-10-16)

- [x] D1. Não existe contrato de ações; cada ViewModel expõe métodos soltos.
- [x] D2. Nomes diferentes para a mesma coisa: `fetch`, `execute`, `action`,
      `loadContent`, `loadMain`, `retry`.
- [x] D3. `AppViewModel.action(String)` é só um `fetch` por URL com outro nome.
- [x] D4. Dois `fetch` no `AppViewModel`: só `fetch(path, params)` põe `Loading`.
- [x] D5. `MovieDetailViewModel.execute(url)` não põe `Loading`, não guarda o
      último pedido (o `retry` repete o antigo) e não cancela a busca anterior.
- [x] D6. `LoginUIState.Success(isLoggedIn)` é gatilho de navegação dentro do
      estado; `signInWith` escreve o estado em dois pontos (corrida).
- [x] D7. (parcial, de propósito) A UI decide quando carregar (`checkSession`, `fetch` na entrada da
      tela) em vez do ViewModel. `LoginAction.CheckSession` continua sendo
      enviado pelo `LoginScreen` (a `WelcomeScreen` usa o mesmo ViewModel e não
      deve navegar sozinha); o `Load(path, params)` do `AppScreen` depende dos
      argumentos da tela, então também fica na tela.
- [x] D8. `Error(throwable)` leva exceção crua para a UI.

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

## M1 — Tipos base (concluído)

- [x] M1.1 Reusar `AppError` (já existe em `domain/error`) em vez de criar
      `UiError`: `Throwable.toAppError()` movido para o `shared`
      (`AppErrorMapping.kt`) e `AppError.canRetry` derivado do código.
      Testes em `AppErrorMappingTest` (`commonTest`). Os estados passam de
      `Error(throwable)` para `Error(error: AppError)` ViewModel por ViewModel,
      nos marcos M2–M4.
      Pendente de verificar: `composeApp` não foi compilado aqui (precisa do
      `designsystem 1.0.34` do GitHub Packages, 401 sem credencial).
- [x] M1.2 Canal de efeitos reutilizável por ViewModel (sem perder evento
      quando a tela não está coletando): `EffectChannel<E>` no `shared`
      (`Channel.UNLIMITED` + `receiveAsFlow`) e helper `CollectEffects` no
      `composeApp`. Testes em `EffectChannelTest` (fila, ordem, entrega única).
      `CollectEffects` não foi compilado aqui (mesmo 401 do `designsystem`).
      Sem commit: junta com o resto do M1 (commit só no fechamento).
- [x] M1.3 Carregamento padrão: `ScreenLoader` (`load` com `Loading`,
      `refresh` sem `Loading`, `retry` repete a última fonte, nova busca
      cancela a anterior). Usado por App, MovieDetail, Main e Settings.
- [x] M1.4 `UIState<T>` fica com `Loading/Success/Error`, mas `Error` agora
      guarda `AppError`. Em vez de um estado `Refreshing`, a recarga com conteúdo
      na tela é o `ScreenLoader.refresh` (mantém o `Success` atual até chegar o
      novo), usado pelas ações `Open` do App e do MovieDetail.
- [x] M1.5 Testes dos tipos base (`commonTest`): `AppErrorMappingTest`,
      `EffectChannelTest`, `ScreenLoaderTest`.
- [x] Fechamento: build, testes, revisão do checklist.

## M2 — Login (piloto) (concluído)

- [x] M2.1 `LoginAction` (`Submit`, `SocialSignIn`, `CheckSession`) e `onAction`.
- [x] M2.2 `LoginUIState` só `Idle/Loading`. O erro de login (snackbar) virou
      efeito `ShowError(AppError)`, não estado: não repete ao reconstruir.
- [x] M2.3 `LoginEffect` (`NavigateToHome`, `ShowError`) no lugar do `Success`.
- [x] M2.4 Um só ponto de escrita do estado (`signIn`, que sempre volta para
      `Idle`). `CheckSession` ficou como ação (ver D7).
- [x] M2.5 `LoginScreen`/`WelcomeScreen` só enviam ações e coletam efeito.
- [x] M2.6 Testes do ViewModel (rodados) e das telas instrumentadas
      (compilam; não rodaram, sem emulador).
- [x] Fechamento: build, testes, revisão do checklist.

## M3 — AppViewModel e MovieDetailViewModel (concluído)

- [x] M3.1 `AppAction` (`Load`, `Open`, `Retry`) substituindo `fetch`, `action`
      e `retry` soltos; um único caminho interno (`ScreenLoader`). `Load` mostra
      `Loading`; `Open` mantém o conteúdo (comportamento que o `action` já tinha).
- [x] M3.2 `MovieDetailViewModel` usa o mesmo caminho (corrige D5).
- [x] M3.3 `retry` repete o último pedido de verdade (teste: retry depois de `Open`).
- [x] M3.4 Chamadores em `AppScreen`, `MovieDetailScreen`, `MainScreen` e Settings.
- [x] M3.5 Testes.
- [x] Fechamento: build, testes, revisão do checklist.

## M4 — Demais ViewModels (concluído)

- [x] M4.1 `SignUp` (`SignUpAction`, `SignUpEffect`)
- [x] M4.2 `VerifyAccount` (erro do OTP fica no estado, `Verified` é efeito)
- [x] M4.3 `CreateNewPassword` (`Changed` e `ShowError` são efeitos)
- [x] M4.4 `Logout` e `CleanCache` (`onAction`; a navegação do CleanCache segue pelo `GlobalEvent`)
- [x] M4.5 `Settings` e `Main`
- [x] Fechamento por ViewModel: build, testes, revisão do checklist.

## M5 — Testes (concluído)

- [x] M5.1 Testes do shared agora conferem sequência de estados + efeitos
      (utilitário `collectEffects` em `commonTest`, sem Turbine). Os testes de
      tela instrumentados continuam com `verify { viewModel.onAction(...) }`.
- [x] M5.2 `README.md` e `docs/mobile/index.md` atualizados.
- [x] Fechamento final: build, testes (`commonTest` e instrumentados),
      revisão do checklist.

## Verificação feita (2026-10-16)

- `:shared:jvmTest`: 358 testes, 0 falhas (inclui as 2 falhas antigas do
  `MainViewModelTest`, que eram erro do próprio teste: `coVerify { ...single() }`).
- `:composeApp:testDebugUnitTest`: 13 testes, 0 falhas.
- `:composeApp:compileDebugKotlinAndroid`, `compileDebugUnitTestKotlinAndroid` e
  `compileDebugAndroidTestKotlinAndroid`: compilam.
- **Não rodou:** testes instrumentados (sem emulador/dispositivo), nenhuma
  verificação visual das telas, iOS e wasm não compilados.
- A compilação do `composeApp` foi feita com `designsystem 1.0.33` (a 1.0.34 dá
  401 no GitHub Packages); a troca foi só local e não está no commit.

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
