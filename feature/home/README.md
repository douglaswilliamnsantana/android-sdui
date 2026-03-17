# feature:home

Módulo de feature responsável pela tela principal do app. Consome `FetchScreenUseCase` do domínio, gerencia o estado da UI e delega a renderização ao engine SDUI.

---

## Responsabilidades

- Buscar o layout da tela home via `FetchScreenUseCase`
- Expor estados de loading, erro e sucesso via `StateFlow`
- Renderizar a árvore de componentes SDUI recebida do servidor

---

## Estrutura

```
feature/home/
└── src/main/kotlin/com/douglassantana/home/
    ├── HomeViewModel.kt   → gerencia estado e chama o use case
    └── HomeScreen.kt      → composable que observa o ViewModel e renderiza
```

---

## Fluxo

```
HomeScreen
    │
    ▼
HomeViewModel ──→ FetchScreenUseCase ──→ SduiRepository ──→ API
    │
    ▼
StateFlow<Node?>
    │
    ├── isLoading = true  → CircularProgressIndicator
    ├── error != null     → mensagem de erro
    └── node != null      → ComponentRegistry → RendererRegistry → Composable
```

---

## HomeViewModel

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchScreen: FetchScreenUseCase,
) : ViewModel()
```

| StateFlow | Tipo | Descrição |
|---|---|---|
| `node` | `Node?` | Árvore de componentes retornada pelo servidor |
| `isLoading` | `Boolean` | `true` enquanto a requisição está em andamento |
| `error` | `String?` | Mensagem de erro caso a requisição falhe |

A rota chamada ao inicializar é `/home`. O servidor mock deve expor esse endpoint.

---

## HomeScreen

```kotlin
@Composable
fun HomeScreen(
    componentRegistry: ComponentRegistry,
    rendererRegistry: RendererRegistry,
    viewModel: HomeViewModel = hiltViewModel(),
)
```

| Estado | Comportamento |
|---|---|
| `isLoading` | Exibe `CircularProgressIndicator` centralizado |
| `error` | Exibe mensagem de erro centralizada |
| `node` disponível | Cria `UIComponent` via `ComponentRegistry` e renderiza via `RendererRegistry` |

---

## Dependências

```kotlin
// feature/home/build.gradle.kts
dependencies {
    implementation(project(":core:domain"))       // FetchScreenUseCase
    implementation(project(":core:sdui-core"))    // Node, ComponentRegistry, SDUIContext
    implementation(project(":core:sdui-runtime")) // RendererRegistry
    implementation(libs.hilt.navigation.compose)  // hiltViewModel()
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
```

---

## Mock Server

O endpoint `/home` é servido pelo mock server local:

**[android-sdui-mock-server](https://github.com/douglaswilliamnsantana/android-sdui-mock-server)**

```bash
git clone https://github.com/douglaswilliamnsantana/android-sdui-mock-server
cd android-sdui-mock-server
npm install && npm start
```

O emulador acessa o servidor via `http://10.0.2.2:3000/screens/home`.

---

## Como adicionar uma nova feature

1. Crie um módulo em `feature/<nome>/` com o plugin `convention.android.library.compose`
2. Declare as dependências necessárias de `core:domain` e `core:sdui-*`
3. Crie o `ViewModel` com `@HiltViewModel` injetando o use case correspondente
4. Crie o composable de tela observando o `ViewModel`
5. Adicione `implementation(project(":feature:<nome>"))` no `app/build.gradle.kts`

---

[← README do projeto](../../README.md)
