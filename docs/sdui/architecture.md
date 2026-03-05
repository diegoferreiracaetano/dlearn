# Arquitetura Server-Driven UI (SDUI)

O DLearn utiliza uma abordagem **SDUI (Server-Driven UI)** para permitir que a interface do aplicativo seja controlada dinamicamente pelo backend.

## 🚀 Conceitos Chave

### 1. BFF (Backend for Frontend)
O BFF (`server/`) atua como um orquestrador, coletando dados de diversas fontes (TMDB, Pokémon API, Banco de Dados Local) e transformando-os em uma estrutura de componentes consumível pelo App.

### 2. Motor de Renderização Genérico
O cliente (`composeApp/`) não possui layouts hardcoded para as telas principais. Em vez disso, utiliza um motor de renderização centralizado no `RenderComponent.kt`.

### 3. Hierarquia Recursiva de Componentes
O componente `AppContainerComponent` permite aninhar outros componentes, possibilitando estruturas complexas como:
- TopBar + Filtros (ChipGroup) + Carrosséis de Vídeo + BottomBar.

## 🔄 Fluxo de Dados

1.  **Request**: O App solicita uma rota (ex: `/home`) ao BFF.
2.  **Orquestração**: O BFF busca os dados, aplica regras de negócio e constrói a `Screen`.
3.  **Response**: O BFF retorna um JSON polimórfico contendo a lista de `Component`.
4.  **Mapeamento**: O `SDUIViewModel` recebe o objeto `Screen`.
5.  **Renderização**: O `RenderComponent` itera sobre a lista de componentes e chama as funções Compose correspondentes no **Design System**.

## 🧩 Componentes Disponíveis

Atualmente, o DLearn suporta os seguintes componentes:

| Componente | Função |
|---|---|
| `AppTopBarComponent` | Barra de topo com título e campo de busca. |
| `BottomNavigationComponent` | Barra de navegação inferior dinâmica. |
| `AppContainerComponent` | Container raiz que pode conter barras de sistema e sub-componentes. |
| `CarouselComponent` | Lista horizontal de itens (`CardComponent`). |
| `BannerCarouselComponent` | Carrossel de banners deslizantes com suporte a vídeo/imagem. |
| `FullScreenBannerComponent` | Banner de tela cheia para destaques principais. |
| `ChipGroupComponent` | Grupo de chips para filtragem rápida. |

## 🛠️ Próximos Passos (TODO)

- [ ] Implementar `SDUIRepositoryImpl` para buscar telas dinamicamente.
- [ ] Criar endpoint `/api/screen/{route}` no servidor para centralizar a busca de telas.
- [ ] Refatorar telas de `Favoritos` e `Conta` para usar o motor SDUI.
