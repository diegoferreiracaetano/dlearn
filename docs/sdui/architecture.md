# Arquitetura SDUI (Server-Driven UI)

O DLearn utiliza uma arquitetura de SDUI onde o servidor (BFF) orquestra não apenas o conteúdo, mas a estrutura e o fluxo de navegação do aplicativo.

---

## 💎 Regras de Ouro (Core Principles)

Para manter a integridade do sistema, as seguintes regras devem ser seguidas rigorosamente:

1.  **Infraestrutura Genérica**: `AppScreen` e `AppViewModel` são componentes de infraestrutura genéricos. **Nunca** crie especializações (ex: `HomeAppScreen`) ou injete repositórios específicos neles. Eles devem permanecer agnósticos ao conteúdo.
2.  **Contrato Único (`AppRequest`)**: Toda comunicação entre App e BFF ocorre via `AppRequest`.
    -   `path`: Define a rota/lógica no servidor.
    -   `params`: Dados contextuais (filtros, IDs).
    -   `metadata`: Informações de rastreio ou estado da sessão.
3.  **Desacoplamento de Lógica**: A lógica de negócio e decisão de "o que exibir" reside 100% no Backend (Orchestrators/Builders). O Frontend é um motor de renderização "burro".
4.  **Zero Hardcode**: Nenhuma cor, string ou dimensão é fixa no código. Tudo deve ser provido por tokens de tema ou via SDUI.

---

## 🔗 O Sistema de "Strings" SDUI

A navegação é baseada em URLs dinâmicas que permitem ao servidor mudar fluxos sem atualizações na loja.

### Formato da Rota
`app?path={path}&params={key1}:{value1},{key2}:{value2}`

### Fluxo de Execução
1.  **Evento de UI**: O usuário clica em um elemento com `actionUrl`.
2.  **ScreenRouter**: O App intercepta a string e a converte em um `AppRequest`.
3.  **BFF Gateway**: O servidor recebe o POST em `/v1/app`.
4.  **Orchestrator**: O backend seleciona o `ScreenBuilder` correto baseado no `path`.
5.  **Resposta Polimórfica**: O servidor retorna um objeto `Screen` contendo uma lista de `Component`.
6.  **Composição**: O Compose percorre a lista e renderiza os componentes nativos correspondentes.

---

## 🛡️ Challenge Engine (MFA)

A segurança é integrada ao fluxo de rede.

-   **428 Precondition Required**: Quando uma ação exige MFA, o servidor retorna este código.
-   **ChallengeInterceptor**: O App captura o erro, exibe o componente de desafio (ex: OTP) e, após o sucesso, **repete a requisição original** automaticamente.

---

## 🛠️ Adicionando Novos Componentes

Para adicionar um novo componente ao ecossistema:
1.  **Defina no `:shared`**: Crie a `interface` ou `data class` herdando de `Component`.
2.  **Mapeie no Backend**: Adicione a lógica de criação no `ScreenBuilder` correspondente.
3.  **Implemente no App**: Crie o `Renderer` (Composable) que sabe transformar o modelo em UI.
4.  **Atualize o Swagger**: Adicione a definição em `documentation.yaml`.
