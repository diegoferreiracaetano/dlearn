# Arquitetura Server-Driven UI (SDUI)

O DLearn utiliza uma abordagem **SDUI (Server-Driven UI)** para permitir que a interface do aplicativo seja controlada dinamicamente pelo backend.

## 🚀 Conceitos Chave

### 1. BFF (Backend for Frontend)
O BFF (`server/`) atua como um orquestrador, coletando dados de diversas fontes (TMDB, Banco de Dados Local) e transformando-os em uma estrutura de componentes consumível pelo App.

### 2. Motor de Renderização Genérico
O cliente (`composeApp/`) não possui layouts hardcoded para as telas principais. Em vez disso, utiliza um motor de renderização centralizado que interpreta o JSON polimórfico vindo do servidor.

### 3. Hierarquia Recursiva de Componentes
O componente `AppContainerComponent` permite aninhar outros componentes, possibilitando estruturas complexas como:
- TopBar + Filtros (ChipGroup) + Carrosséis de Vídeo + BottomBar.

## 🔄 Fluxo de Dados

1.  **Request**: O App solicita uma rota (ex: `/v1/home`) ao BFF.
2.  **Orquestração**: O BFF busca os dados, aplica regras de negócio e constrói a `Screen`.
3.  **Response**: O BFF retorna um JSON polimórfico contendo a lista de `Component`.
4.  **Mapeamento**: O `SDUIViewModel` recebe o objeto `Screen`.
5.  **Renderização**: O `ComponentRenderer` itera sobre a lista de componentes e chama as funções Compose correspondentes no **Design System**.

## 🧩 Componentes Disponíveis

Atualmente, o DLearn suporta os seguintes componentes:

| Componente | Função |
|---|---|
| `AppTopBarComponent` | Barra de topo com título, subtítulo e imagem opcional. |
| `AppTopBarListComponent` | Variação da barra de topo para listas. |
| `AppSearchBarComponent` | Barra de pesquisa com slot para injeção dinâmica de resultados. |
| `AppSearchContentComponent` | Marcador para resultados de busca dentro do SearchBar. |
| `AppEmptyStateComponent` | Feedback visual para estados vazios ou não encontrados. |
| `AppLoadingComponent` | Indicador de carregamento. |
| `AppErrorComponent` | Feedback visual para estados de erro. |
| `AppFeedbackComponent` | Mensagens de feedback (sucesso/aviso) para o usuário. |
| `BottomNavigationComponent` | Barra de navegação inferior dinâmica. |
| `AppContainerComponent` | Container raiz para telas complexas (TopBar + BottomBar + Content). |
| `AppMainContentComponent` | Marcador de conteúdo principal dentro de containers. |
| `CarouselComponent` | Carrossel genérico para qualquer lista de componentes. |
| `MovieCarouselComponent` | Carrossel especializado para posters de filmes/séries. |
| `BannerCarouselComponent` | Carrossel de banners de destaque. |
| `FullScreenBannerComponent` | Banner de tela cheia para lançamentos. |
| `ChipGroupComponent` | Grupo de chips para filtros (ex: categorias, gêneros). |
| `UserRowComponent` | Linha informativa (ex: atores, membros da equipe). |
| `ProfileRowComponent` | Item de menu/perfil com informações do usuário. |
| `PremiumBannerComponent` | Banner promocional para assinatura Premium. |
| `SectionComponent` | Lista de itens agrupados por uma seção com título. |
| `AppMovieDetailHeaderComponent` | Header detalhado de mídia (poster, trailer, elenco, provedores). |
| `AppExpandableSectionComponent` | Texto expansível para sinopses e descrições longas. |
| `FooterComponent` | Rodapé simples com label e ação. |

## 🛠️ Navegação Genérica

O DLearn implementa um gateway `/v1/app` que resolve rotas dinâmicas.
Ao navegar para `app/watchlist`, o App faz um POST para o BFF, que decide qual lógica executar e qual tela retornar, permitindo mudar fluxos sem atualizar o binário do app.
