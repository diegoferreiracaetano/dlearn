# Módulo Server (Ktor BFF)

O servidor do DLearn atua como um **Backend for Frontend (BFF)**, fornecendo a estrutura de dados e UI que o aplicativo consome.

## 🏗️ Camadas do Servidor

O servidor segue uma estrutura de **Clean Architecture**:

- `api/controllers/`: Define os endpoints e recebe requisições (ex: `HomeController.kt`, `AppController.kt`).
- `orchestrator/`: A camada principal de lógica de negócio do BFF. Coordena a busca de dados e a construção da tela (`Screen`).
- `domain/`: Entidades de negócio e interfaces dos repositórios.
- `infrastructure/`: Implementações de repositórios que acessam APIs de terceiros (TMDB) e gerenciam o cache de memória.
- `ui/screens/`: Builders que transformam dados de domínio em componentes SDUI.

## 📡 Endpoints Principais

- `GET /v1/main`: Retorna a casca (shell) principal do app (BottomNav + Container).
- `GET /v1/home`: Retorna a configuração completa da tela inicial.
- `GET /v1/search/main`: Retorna a tela inicial de busca.
- `GET /v1/search/result?q={query}`: Retorna os resultados da busca.
- `GET /v1/movie/{movieId}`: Detalhes de um filme/série específicos.
- `GET /v1/profile`: Dados e estrutura da tela de perfil.
- `POST /v1/app`: **Gateway SDUI**. Resolve rotas dinâmicas como `favorite` e `watchlist`.

## ⚡ Performance & Cache

O projeto utiliza um sistema de **Cache** genérico para armazenar objetos de tela renderizados, reduzindo o número de chamadas para APIs externas.

Além disso, o servidor está preparado para lidar com múltiplos idiomas através do header `Accept-Language` e versões de app via `X-App-Version`.

## 🏃 Como Rodar Localmente

1. No terminal, execute o comando:
   ```bash
   ./gradlew :server:run
   ```
2. O servidor iniciará por padrão na porta `8081`.
3. Acesse a documentação Swagger em: `http://localhost:8081/swagger`.
