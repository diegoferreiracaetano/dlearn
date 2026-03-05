# Módulo ComposeApp (Mobile)

O módulo **ComposeApp** é a aplicação principal desenvolvida com **Jetpack Compose** (via Compose Multiplatform), responsável por renderizar a interface baseada nos dados do BFF.

## 🏗️ Estrutura do Módulo

O módulo segue a arquitetura **MVVM (Model-View-ViewModel)** com os seguintes subpacotes:

- `ui/screens/`: Contém as telas do aplicativo (ex: `HomeScreen`, `SplashScreen`).
- `ui/util/`: Onde residem as utilidades de renderização (**RenderComponent**).
- `ui/theme/`: Definições do Design System (Cores, Tipografia, Formas).
- `navigation/`: Gerenciamento de rotas e navegação entre telas via Compose Navigation.
- `di/`: Módulo de Injeção de Dependência (Koin) específico para o App.

## 🚀 Motor SDUI (Renderer)

O `RenderComponent.kt` é o motor genérico que converte componentes SDUI em Composables reais do Design System.

**Exemplo de Implementação:**

```kotlin
@Composable
fun RenderComponent(
    component: Component,
    onItemClick: (String) -> Unit,
    // ... outros callbacks
) {
    when (component) {
        is CarouselComponent -> {
            Carousel(
                title = component.title,
                items = component.items.map { it.toDsCarouselItem(onItemClick) }
            )
        }
        is BannerComponent -> {
            BannerCard(
                title = component.title,
                imageUrl = component.imageUrl,
                onClick = { component.actionUrl?.let { onItemClick(it) } }
            )
        }
        // ... outros componentes
    }
}
```

## 🧩 Injeção de Dependência

Utilizamos o **Koin 4.1.0** para injetar os ViewModels e Repositórios no App. Cada tela principal possui seu próprio ViewModel que consome a API do BFF.

## 🛠️ Próximos Passos (TODO)

- [ ] Unificar todos os ViewModels em um `SDUIViewModel` genérico.
- [ ] Refatorar a navegação para que o backend decida para onde cada clique leva o usuário.
- [ ] Adicionar suporte para componentes de formulário e ações complexas via SDUI.
