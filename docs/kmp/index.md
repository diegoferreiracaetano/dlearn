# Módulo Shared (KMP)

O módulo **Shared** é o coração do projeto DLearn, onde toda a lógica de negócio e os modelos de dados residem, garantindo consistência entre as plataformas Android, iOS e Web.

## 🏗️ Estrutura do Módulo

O módulo segue a arquitetura **Domain-Driven Design (DDD)** com os seguintes subpacotes:

- `domain/`: Contém as interfaces dos repositórios, Use Cases e as entidades puras de negócio (ex: `User`, `Video`, `Pokemon`).
- `data/`: Implementações dos repositórios, acessando APIs externas via Ktor Client ou bancos de dados locais.
- `ui/sdui/`: Onde residem as definições dos componentes da UI dinâmica (**Server-Driven UI**).

## 🧩 Modelos SDUI

Os componentes SDUI são implementados como uma hierarquia de interfaces seladas (`sealed interface Component`), permitindo o polimorfismo seguro durante a serialização e desserialização via **Kotlinx Serialization**.

**Principais Modelos:**
- `Screen`: A estrutura raiz retornada pelo backend para uma rota específica. Contém uma lista de `Component`.
- `AppContainerComponent`: Um componente recursivo especial que suporta `TopBar`, `BottomBar` e outros sub-componentes.
- `CarouselComponent`: Representa uma lista horizontal de itens (`CardComponent`).
- `BannerComponent`: Um banner simples com imagem e texto.
- `ChipGroupComponent`: Uma lista horizontal de filtros rápidos.

## 📦 Injeção de Dependência

Utilizamos o **Koin 4.1.0** para gerenciar as dependências de forma compartilhada. O `sharedModule` expõe os repositórios e serviços que serão injetados nos ViewModels dos aplicativos clientes.

```kotlin
// Exemplo de configuração no sharedModule
val sharedModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<SDUIRepository> { SDUIRepositoryImpl(get()) }
}
```
