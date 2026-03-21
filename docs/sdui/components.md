# Componentes SDUI

Esta seção documenta os componentes de UI disponíveis que podem ser enviados pelo servidor e renderizados pelo aplicativo.

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
