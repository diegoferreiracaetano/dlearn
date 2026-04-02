# Módulo ComposeApp (Mobile Multiplatform)

O **ComposeApp** é o motor de renderização nativo (Android e iOS) do ecossistema DLearn, responsável
por transformar o JSON polimórfico do BFF em uma interface de usuário fluida e de alta performance
utilizando **Compose Multiplatform**.

---

## 🎨 O Design System (A Base de Tudo)

O App segue a regra de **Zero Hardcode**. Toda a UI é construída sobre componentes atômicos que
consomem o tema do projeto.

- **`ui/theme/`**: Centraliza cores, tipografia e formas, garantindo consistência visual entre
  Android e iOS.
- **Componentes Atômicos**: Botões, Cards, TextFields, etc., que recebem dados via SDUI e os
  renderizam.

---

## 🚀 Motor de Renderização SDUI

O App não possui telas fixas para a maioria dos fluxos. Ele utiliza um motor genérico que decide
qual Composable exibir baseado no tipo do componente retornado pelo servidor.

### O Papel do `AppScreen` e `AppViewModel`

Seguindo as **Regras de Ouro**, estas são classes de infraestrutura genéricas:

1. **`AppViewModel`**: Recebe um `AppRequest`, gerencia o estado da rede (Loading, Success, Error) e
   armazena a lista de componentes da `Screen`.
2. **`AppScreen`**: O container que observa o ViewModel e delega a renderização para o
   `RenderComponent`.

### `RenderComponent.kt`

Este é o "cérebro" visual. Ele percorre a lista de `Component` e utiliza um `when` exaustivo para
instanciar o Composable correto:

```kotlin
when (component) {
    is MovieCarouselComponent -> MovieCarousel(component.items)
    is AppTopBarComponent -> TopBar(component.title, component.subtitle)
    is AppTextFieldComponent -> AppTextField(component)
    // ... outros componentes
}
```

---

## 🔗 Navegação e Ações

O App é um "navegador" de strings SDUI.

1. Um componente (ex: Botão) recebe um `actionUrl` (ex:
   `app?path=movie/detail&params=movieId:157336`).
2. Ao clicar, o **`ScreenRouter`** converte essa string em um novo `AppRequest`.
3. O App navega para uma nova instância de `AppScreen` passando o request.
4. O ciclo SDUI recomeça.

---

## 🛡️ Gestão de Desafios (MFA)

O App possui um sistema de interceptação de erros de rede que automatiza a segurança.

- Se uma requisição falha com `428`, o **`ChallengeHandler`** abre automaticamente o diálogo
  correto (OTP, Biometria).
- Após a resolução, o fluxo continua sem que o usuário perceba que a requisição foi interrompida.

---

## 🛠️ Como Adicionar UI no App

1. **Crie o Composable**: No Design System, seguindo os padrões visuais.
2. **Mapeie no Renderer**: Adicione o `is NovoComponente -> ...` no `RenderComponent.kt`.
3. **Teste**: Use o `@Preview` para validar o visual sem precisar rodar o backend.
