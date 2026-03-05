# DLearn - Documentação Técnica

Bem-vindo à documentação oficial do projeto **DLearn**, um ecossistema desenvolvido com **Kotlin Multiplatform (KMP)** focado em **Server-Driven UI (SDUI)**.

O objetivo do DLearn é fornecer uma experiência de usuário dinâmica e personalizável, onde a interface é ditada pelo backend, permitindo atualizações em tempo real sem a necessidade de novos deploys nas lojas de aplicativos.

## 📌 Visão Geral do Projeto

O projeto é dividido em três pilares principais:

1.  **[Módulo Shared (KMP)](kmp/index.md)**: Onde residem os modelos de dados, contratos de componentes SDUI e lógica de negócio compartilhada entre Android, iOS e Web.
2.  **[Módulo Server (Ktor)](server/index.md)**: O Backend for Frontend (BFF) que orquestra os dados e constrói a estrutura da UI.
3.  **[Módulo ComposeApp](mobile/index.md)**: O cliente que consome o BFF e renderiza a interface dinamicamente através de um motor genérico.

## 🚀 Arquitetura SDUI

A arquitetura SDUI do DLearn baseia-se em:
- **Componentes Atômicos**: Cada elemento de UI (Banner, Carrossel, Chip) é um objeto serializável.
- **Renderização Recursiva**: O `RenderComponent` lida com containers aninhados de forma genérica.
- **Navegação Baseada em Rotas**: O backend define para onde cada ação leva o usuário.

[Saiba mais sobre a implementação SDUI aqui.](sdui/architecture.md)
