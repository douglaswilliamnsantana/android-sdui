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
    ├── HomeScreen.kt      → composable que observa o ViewModel e renderiza
    └── di/
        └── HomeModule.kt  → registra o HomeViewModel no grafo Koin
```

---

## Fluxo

```
LauncherScreen (feature:launcher) ──→ escolhe ScreenSource
    │
    ▼
HomeScreen(source)
    │
    ▼
HomeViewModel ──→ FetchScreenUseCase ──→ SduiRepository (Backend ou RemoteConfig) ──→ API / Remote Config
    │
    ▼
StateFlow<ScreenUiState<Node>>
    │
    ├── Loading         → CircularProgressIndicator
    ├── Error(message)  → mensagem de erro
    └── Success(data)   → ComponentRegistry → RendererRegistry → Composable
```

`ScreenUiState<T>` (definida em `core:sdui-core`, `com.douglassantana.sdui_core.state`) é genérica e reutilizável por qualquer tela SDUI — não é específica da home. `message` em `Error` já vem amigável para o usuário: ambas as implementações de `SduiRepository` mapeiam exceções técnicas (timeout, erro de serialização etc.) para `SduiError` (`core:domain`) antes de chegar ao ViewModel.

`ScreenSource` (`core:domain`, `Backend` ou `RemoteConfig`) decide *qual* `SduiRepository` o Koin injeta em `FetchScreenUseCase` — escolhido pelo usuário em `LauncherScreen` (módulo `feature:launcher`) e passado como parâmetro para `HomeScreen`. `HomeViewModel` nunca sabe qual fonte foi escolhida; a seleção acontece inteiramente na resolução do Koin (`di/HomeModule.kt` e `core:data/di/DataModule.kt`).

---

## HomeViewModel

```kotlin
class HomeViewModel(
    private val fetchScreen: FetchScreenUseCase,
) : ViewModel()
```

Registrado em `home.di.HomeModule.kt`, parametrizado por `ScreenSource`:

```kotlin
val homeModule = module {
    viewModel { (source: ScreenSource) ->
        HomeViewModel(fetchScreen = get { parametersOf(source) })
    }
}
```

| StateFlow | Tipo | Descrição |
|---|---|---|
| `uiState` | `ScreenUiState<Node>` | `Loading`, `Error(message)` ou `Success(data: Node)` (sealed interface em `core:sdui-core`) |

A rota chamada ao inicializar é `Route.Home` (`"/home"`, `core:domain`). O servidor mock deve expor esse endpoint.

---

## HomeScreen

```kotlin
@Composable
fun HomeScreen(
    componentRegistry: ComponentRegistry,
    rendererRegistry: RendererRegistry,
    source: ScreenSource,
    viewModel: HomeViewModel = koinViewModel(parameters = { parametersOf(source) }),
)
```

| Estado (`ScreenUiState<Node>`) | Comportamento |
|---|---|
| `Loading` | Exibe `CircularProgressIndicator` centralizado |
| `Error(message)` | Exibe `message` centralizado |
| `Success(data)` | Cria `UIComponent` via `ComponentRegistry` (a partir de `data`) e renderiza via `RendererRegistry` |

---

## Dependências

```kotlin
// feature/home/build.gradle.kts
dependencies {
    implementation(project(":core:domain"))       // FetchScreenUseCase
    implementation(project(":core:sdui-core"))    // Node, ComponentRegistry, SDUIContext
    implementation(project(":core:sdui-runtime")) // RendererRegistry
    implementation(libs.koin.android)              // viewModelOf()
    implementation(libs.koin.androidx.compose)     // koinViewModel()
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
```

---

## Mock Server

Só necessário quando `source = ScreenSource.Backend`. O endpoint `/home` é servido pelo mock server local:

**[android-sdui-mock-server](https://github.com/douglaswilliamnsantana/android-sdui-mock-server)**

```bash
git clone https://github.com/douglaswilliamnsantana/android-sdui-mock-server
cd android-sdui-mock-server
npm install && npm start
```

O emulador acessa o servidor via `http://10.0.2.2:3000/screens/home`.

`source = ScreenSource.RemoteConfig` não precisa do mock server rodando: busca a tela do Firebase Remote Config real. Setup e como publicar dados de teste: [`docs/firebase.md`](../../docs/firebase.md).

---

## Como adicionar uma nova feature

1. Crie um módulo em `feature/<nome>/` com o plugin `convention.android.library.compose`
2. Declare as dependências necessárias de `core:domain` e `core:sdui-*`
3. Crie o `ViewModel` recebendo o use case correspondente no construtor e registre-o com `viewModelOf(::SeuViewModel)` em um módulo Koin
4. Crie o composable de tela observando o `ViewModel`
5. Adicione `implementation(project(":feature:<nome>"))` no `app/build.gradle.kts`

---

[← README do projeto](../../README.md) · [feature:launcher](../launcher/README.md)
