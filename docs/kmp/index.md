# Módulo Shared (KMP)

O módulo **Shared** é o alicerce do ecossistema DLearn, garantindo que a lógica de negócio, modelos de dados e o contrato SDUI sejam idênticos em todas as plataformas (Android, iOS e Server).

---

## 🏗️ Estrutura e Camadas

O módulo segue o padrão de **Clean Architecture** compartilhado:

### 1. `ui/sdui/` (Contrato de UI)
Contém as definições polimórficas de todos os componentes visuais.
- **`Component`**: Interface selada raiz para todos os elementos de UI.
- **`AppRequest`**: Modelo canônico para requisições ao BFF (path, params, metadata).
- **`Screen`**: Estrutura de resposta que contém a árvore de componentes.

### 2. `data/` (Rede e Repositórios)
Implementação do cliente HTTP utilizando **Ktor Client**.
- **`ChallengeInterceptor`**: O "mecanismo secreto" que gerencia o fluxo de MFA de forma transparente para as outras camadas.
- **`AuthRepository`**: Gerenciamento de tokens (JWT) e sessão do usuário.

### 3. `domain/` (Entidades e Use Cases)
Regras de negócio puras que não dependem de nenhuma plataforma ou framework.

---

## 🧩 O Motor SDUI no Shared

O Shared provê as ferramentas para que o App processe a "String SDUI".

### Como o `AppRequest` é Criado
Utilizamos o helper `AppRequest.fromUrl(url)` para converter a string de navegação em um objeto estruturado:
```kotlin
val request = AppRequest.fromUrl("app?path=home&params=filter:movies")
// Resulta em: path="home", params={filter="movies"}
```

### Serialização Polimórfica
Utilizamos **Kotlinx Serialization** com `@JsonClassDiscriminator("type")` para que o App consiga desserializar automaticamente a lista de componentes vinda do servidor para os objetos Kotlin correspondentes.

---

## 📦 Injeção de Dependência (Koin)

O `sharedModule` expõe os repositórios e serviços necessários para os aplicativos clientes.

```kotlin
val sharedModule = module {
    single { createHttpClient(get()) }
    single<SDUIRepository> { SDUIRepositoryImpl(get()) }
    factory { AppViewModel(get()) } // ViewModel Genérico
}
```

---

## 🛠️ Como Contribuir no Shared
- **Novos Componentes**: Devem ser criados primeiro aqui para que tanto o Server quanto o Mobile reconheçam o contrato.
- **Regras de Negócio**: Se a lógica deve funcionar no Android e iOS, ela **deve** estar no Shared.
