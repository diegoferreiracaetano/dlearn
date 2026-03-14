# Contexto e Regras do Projeto (KMP)

Deve-se seguir estas diretrizes rigorosamente. Se não for possível cumpri-las, peça esclarecimentos.

## 1. Estrutura e Arquitetura
- Projeto **Kotlin Multiplatform (KMP)**.
- Respeitar a estrutura de pastas atual (`commonMain`, `androidMain`, etc.).
- Aplicar **Clean Code** e SOLID.

## 2. Design System (REGRA CRÍTICA)
- Usar **APENAS** componentes do Design System do projeto.
- **BLOQUEIO:** Se um componente necessário não existir, **PARAR A GERAÇÃO IMEDIATAMENTE**.
- Avisar: "Componente [nome] não encontrado no Design System" e sugerir a criação do componente antes de prosseguir.
- **Zero Hardcode:** Proibidas strings, cores ou dimensões fixas. Usar tokens de tema e recursos (ex: MOKO ou BuildKonfig).

## 3. Backend e Front
- Siga a mesma arquitetura já existe no projeto.
- Use sempre a estrutura de Component aonde ele gera um ScreenBuilder para cada tela.
- No Front consuma o monta as telas dimanicamente,
- No Front nunca crie que não consta no backend se necessario  e sugerir a criação do componente antes de prosseguir.


## 4. Testes e Documentação
- Gerar sempre testes unitários no `commonTest`.
- Atualizar o `README.md` para novas funcionalidades.
- Manter as especificações **OpenAPI / Swagger** sincronizadas com mudanças em modelos ou redes.
