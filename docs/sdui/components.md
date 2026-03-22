# Componentes SDUI

Esta seção documenta os componentes de UI disponíveis que podem ser enviados pelo servidor e renderizados pelo aplicativo.

## AppHtmlTextComponent

Componente utilizado para renderizar conteúdo HTML básico. Ideal para telas de termos de uso, políticas de privacidade e seções de ajuda que requerem formatação de texto (negrito, itálico, links, etc.).

### Estrutura (JSON)
```json
{
  "type": "AppHtmlTextComponent",
  "html": "<h1>Política de Privacidade</h1><p>Este é um <b>exemplo</b> de texto com link <a href='https://google.com'>clique aqui</a>.</p>"
}
```

### Propriedades
| Propriedade | Tipo | Obrigatório | Descrição |
| :--- | :--- | :--- | :--- |
| `html` | String | Sim | String contendo as tags HTML a serem renderizadas. |

### Comportamento no App
O `AppHtmlTextRenderer` utiliza o componente `AppHtmlText` do Design System para processar as tags. Suporta tags como `h1`, `h2`, `b`, `i`, `u`, `a`, `br`.

---

## AppSnackbarComponent

Componente utilizado para exibir mensagens temporárias de feedback ao usuário (Sucesso, Erro ou Alerta). Este componente é renderizado utilizando o `SnackbarHost` do sistema.

### Estrutura (JSON)
```json
{
  "type": "AppSnackbarComponent",
  "message": "Perfil atualizado com sucesso!",
  "snackbarType": "SUCCESS",
  "durationMillis": 3000
}
```

### Propriedades
| Propriedade | Tipo | Obrigatório | Descrição |
| :--- | :--- | :--- | :--- |
| `message` | String | Sim | O texto que será exibido no Snackbar. |
| `snackbarType` | Enum (`SUCCESS`, `ERROR`, `WARNING`) | Não | Define o estilo visual e comportamento do snackbar. Padrão: `SUCCESS`. |
| `durationMillis` | Long | Não | Duração da exibição em milissegundos. Padrão: `3000`. |

### Comportamento no App
O `AppSnackbarRenderer` intercepta este componente e utiliza o prefixo esperado pelo Design System (`TYPE:MESSAGE`) para garantir a coloração correta (ex: vermelho para `ERROR`).

---
