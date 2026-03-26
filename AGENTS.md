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
- No Front consuma o monta as telas dimanicamente.
- No Front nunca crie componentes que não constam no backend; se necessário, sugira a criação do componente no backend antes de prosseguir.

## 4. Fluxo de SDUI, AppScreen e AppViewModel (REGRA DE OURO)
- **PROIBIÇÃO DE REIMPLEMENTAÇÃO:** `AppScreen` e `AppViewModel` são componentes genéricos de infraestrutura SDUI e são usados em múltiplos fluxos críticos. **NUNCA** crie novas implementações (ex: `HomeAppScreen`, `SearchAppViewModel`) ou altere suas assinaturas para injetar repositórios específicos.
- **CONTRATO AppRequest:** Toda a comunicação entre o frontend e o backend SDUI deve ser feita exclusivamente via `AppRequest`, passando obrigatoriamente `path`, `params` e `metadata` quando necessário.
- **GESTÃO DE PARÂMETROS:** Alterações de estado na tela (filtros, paginação, buscas) devem ser refletidas em atualizações nos `params` do `AppRequest`. O `AppViewModel` deve ser usado para disparar novas requisições com esses parâmetros atualizados.
- **DESACOPLAMENTO:** A lógica de qual dado carregar reside no Backend (Orchestrators). O Frontend apenas renderiza o que o `AppRequest` retorna.

## 5. Testes e Documentação
- Gerar sempre testes unitários no `commonTest`.
- Atualizar o `README.md` para novas funcionalidades.
- Manter as especificações **OpenAPI / Swagger** sincronizadas com mudanças em modelos ou redes.
