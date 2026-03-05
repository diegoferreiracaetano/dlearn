# Módulo Server (Ktor BFF)

O servidor do DLearn atua como um **Backend for Frontend (BFF)**, fornecendo a estrutura de dados e UI que o aplicativo consome.

## 🏗️ Camadas do Servidor

O servidor segue uma estrutura de **Clean Architecture**:

- `api/controllers/`: Define os endpoints e recebe requisições (ex: `HomeController.kt`).
- `orchestrator/`: A camada principal de lógica de negócio do BFF. Coordena a busca de dados e a construção da tela (`Screen`).
- `domain/`: Entidades de negócio e interfaces dos repositórios.
- `infrastructure/`: Implementações de repositórios que acessam APIs de terceiros (TMDB) e gerenciam o cache de memória.

## 📡 Endpoints Principais

- `GET /v1/home`: Retorna a configuração completa da tela inicial.
- `GET /v1/search`: Retorna os resultados da busca formatados para SDUI.
- `GET /v1/favorites`: Retorna a lista de favoritos do usuário.
- `GET /v1/account`: Retorna os detalhes da conta do usuário.

## ⚡ Performance & Cache

O projeto utiliza **InMemoryCache<K, V>** genérico que armazena os objetos de tela renderizados por **5 minutos**, reduzindo drasticamente o número de chamadas para APIs externas.

Além disso, o header `Cache-Control: max-age=300` é enviado automaticamente em todas as respostas JSON, permitindo que o App gerencie seu próprio cache HTTP de forma eficiente.

## 🏃 Como Rodar Localmente

1. No terminal, execute o comando:
   ```bash
   ./gradlew :server:run
   ```
2. O servidor iniciará por padrão na porta `8081`.
3. Acesse a documentação Swagger em: `http://localhost:8081/swagger`.
