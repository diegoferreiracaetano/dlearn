# Catálogo de Componentes SDUI

O ecossistema DLearn utiliza componentes atômicos e moleculares definidos no módulo `:shared` e
renderizados nativamente via Jetpack Compose (Android/iOS) seguindo as regras do **Design System**.

---

## 🏗️ Estruturas de Layout

### **AppContainerComponent**

O componente raiz para a maioria das telas. Ele orquestra as barras de sistema e o conteúdo
principal.

- **`topBar`**: Componente de cabeçalho (`AppTopBarComponent` ou `AppTopBarListComponent`).
- **`bottomBar`**: Componente de navegação inferior (`BottomNavigationComponent`).
- **`components`**: Lista de componentes que compõem o corpo da tela.

### **AppListComponent**

Um container vertical simples que renderiza uma lista de sub-componentes.

- **`components`**: Lista de componentes a serem renderizados sequencialmente.

---

## 🔝 Cabeçalhos e Navegação

### **AppTopBarComponent**

Barra de topo padrão para telas de conteúdo.

- **`title`**: Título principal.
- **`subtitle`**: Subtítulo opcional.
- **`imageUrl`**: Avatar ou ícone do usuário.
- **`showSearch`**: Booleano para exibir ícone de lupa.

### **AppTopBarListComponent**

Versão especializada para telas que possuem abas ou filtros de navegação no topo.

- **`items`**: Lista de `AppTopBarItem` (contendo rota e configuração da barra).
- **`selectedRoute`**: A rota que deve aparecer como selecionada.

---

## 🎬 Componentes de Mídia

### **MovieCarouselComponent**

Carrossel horizontal otimizado para exibição de posters de filmes.

- **`title`**: Título da seção (ex: "Populares").
- **`items`**: Lista de `movie_item`.

### **movie_item**

O átomo de dados para filmes e séries.

- **`title`**, **`imageUrl`**, **`rating`**, **`year`**: Metadados básicos.
- **`actionUrl`**: Rota SDUI para abrir o detalhe (ex: `app?path=movie/detail&params=movieId:123`).
- **`isPremium`**: Exibe badge de conteúdo exclusivo.

### **AppMovieDetailHeaderComponent**

Componente complexo para o topo da tela de detalhes.

- **`imageUrl`**: Poster de fundo.
- **`trailerId`**: ID para reprodução de vídeo.
- **`providers`**: Lista de plataformas onde o título está disponível (Netflix, HBO, etc).

---

## 📝 Entradas e Formulários

### **AppTextFieldComponent**

Campo de entrada de texto padronizado.

- **`label`**, **`placeholder`**: Textos de auxílio.
- **`fieldType`**: Enum (`TEXT`, `EMAIL`, `PASSWORD`, `PHONE`, `NUMBER`).
- **`key`**: Identificador para o envio de dados no formulário.

### **AppSwitchRowComponent**

Linha com interruptor para configurações.

- **`title`**, **`subtitle`**: Descrição da configuração.
- **`preferenceKey`**: Chave vinculada ao `AppPreferences` local.

---

## ℹ️ Informação e Feedback

### **AppHtmlTextComponent**

Renderizador de HTML seguro para textos longos e formatados.

- **`html`**: String com tags suportadas (`<b>`, `<a>`, `<h1>`).

### **AppSnackbarComponent**

Mensagem efêmera de feedback.

- **`message`**: Texto da mensagem.
- **`snackbarType`**: `SUCCESS`, `ERROR`, `WARNING`.

### **AppFeedbackComponent**

Tela inteira ou seção de feedback com imagem e botões de ação.

- **`title`**, **`description`**: Mensagem principal.
- **`imageSource`**: Ilustração central.
- **`primaryText`**, **`secondaryText`**: Labels dos botões de ação.

---

## 🛠️ Utilitários

| Componente                   | Uso                                                      |
|------------------------------|----------------------------------------------------------|
| **AppLoadingComponent**      | Exibe um skeleton ou spinner de carregamento.            |
| **AppErrorComponent**        | Exibe estado de erro com botão de "Tentar Novamente".    |
| **AppEmptyStateComponent**   | Usado quando uma busca ou lista não retorna resultados.  |
| **AppSectionTitleComponent** | Título de seção simples com tipografia do Design System. |
| **FooterComponent**          | Rodapé para telas de formulário ou informativas.         |
