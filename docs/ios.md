[← Índice](README.md) · [README do projeto](../README.md)

---

# iOS — Integração KMP

Documentação da camada iOS: como o framework KMP é integrado ao Xcode, como funciona o MVVM em SwiftUI e como adicionar novos componentes SDUI.

---

## Visão geral

O iOS consome o módulo `shared` compilado como framework estático (`Shared.framework`). Todo o código de rede, domínio e modelos é compartilhado com o Android via Kotlin Multiplatform.

```
iosApp (SwiftUI)
    │
    └── Shared.framework  ←  compilado pelo Gradle (:shared)
            │
            ├── SduiSdk          ← entry point: fetchScreen() (backend) / parseScreen() (Remote Config)
            ├── NodeReader       ← lê props de forma Swift-friendly
            ├── FetchScreenUseCase
            ├── SduiRepositoryImpl  (Ktor + Darwin engine)
            ├── NodeMapper
            └── modelos: Node, NodeDto, SduiColor, SduiColorTokens…
```

---

## Estrutura de arquivos iOS

```
iosApp/
└── iosApp/
    ├── iOSApp.swift              ← @main, chama AppKoin.shared.start(), hospeda RootView
    ├── Launcher/
    │   └── LauncherView.swift    ← escolha da fonte (Backend / Remote Config), sem estado próprio
    ├── Home/
    │   ├── HomeViewModel.swift   ← ObservableObject, chama SduiSdk
    │   ├── HomeView.swift        ← SwiftUI View, observa HomeViewModel
    │   └── SduiNodeView.swift    ← renderizador recursivo de Node
    └── Theme/
        └── SduiTheme.swift       ← bridge de tokens de design KMP → SwiftUI
```

`RootView` (definida em `iOSApp.swift`) alterna entre `LauncherView` e `HomeView` de forma local (`@State`), espelhando o switch feito no Android por `MainActivity`. `ScreenSourceOption` (definida em `LauncherView.swift`) é um enum Swift local — não é o tipo Kotlin `ScreenSource` — pelo mesmo motivo que `HomeViewModel` nunca importa tipos sealed/genéricos do Kotlin diretamente (ver nota sobre `@Throws`/exportação mais abaixo).

---

## MVVM no iOS

O padrão é análogo ao Android. A tabela abaixo mostra a correspondência direta:

| Conceito | Android (Kotlin) | iOS (Swift) |
|---|---|---|
| Estado reativo | `StateFlow` | `@Published` |
| ViewModel | `ViewModel` (Koin `viewModelOf`) | `ObservableObject` |
| Injeção de dependência | Koin (`by inject()` / `getAll()`) | Koin (`KoinPlatform.getKoin()`) — mesmo grafo do Android |
| Observar na View | `collectAsState()` | `@StateObject` |
| Lançar coroutine | `viewModelScope.launch` | `Task { await ... }` |
| Thread da UI | automático (Compose) | `@MainActor` |
| Ciclo de vida | `init { loadScreen() }` | `init { Task { await loadScreen() } }` |

### HomeViewModel.swift

```swift
@MainActor
final class HomeViewModel: ObservableObject {

    @Published private(set) var node: NodeReader? = nil
    @Published private(set) var isLoading: Bool = true
    @Published private(set) var error: String? = nil

    private let source: ScreenSourceOption
    private let sdk: SduiSdk

    init(source: ScreenSourceOption, sdk: SduiSdk = SduiSdk()) {
        self.source = source
        self.sdk = sdk
        Task { await loadScreen() }
    }

    func retry() {
        Task { await loadScreen() }
    }

    private func loadScreen() async {
        isLoading = true
        error = nil
        do {
            switch source {
            case .backend:
                node = try await sdk.fetchScreen(route: "/home")
            case .remoteConfig:
                // Fetched directly via the native Firebase iOS SDK, not through Kotlin —
                // "home" must stay in sync with Android's key derivation
                // (route.path.removePrefix("/") in RemoteConfigSduiRepositoryImpl).
                let remoteConfig = RemoteConfig.remoteConfig()
                _ = try await remoteConfig.fetchAndActivate()
                let json = remoteConfig.configValue(forKey: "home").stringValue
                node = try sdk.parseScreen(json: json)
            }
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
```

O `.backend` case chama `SduiSdk.fetchScreen`, que faz toda a busca em Kotlin (Ktor). O `.remoteConfig` case é assimétrico de propósito: o Firebase iOS SDK é um Swift Package, não é facilmente cinterop-ável a partir do Kotlin/Native sem ferramental extra — então o Swift busca o Remote Config direto pelo SDK nativo e só pede pro `SduiSdk` fazer o parsing do JSON (`parseScreen`, reaproveitando `NodeMapper`/`SduiJson` do Kotlin em vez de duplicar esse parsing em Swift). No Android, as duas fontes passam pelo mesmo `FetchScreenUseCase` (ver `docs/firebase.md`), porque o SDK Android do Firebase é Kotlin puro — sem essa restrição.

### HomeView.swift

```swift
struct HomeView: View {
    @StateObject private var viewModel: HomeViewModel

    init(source: ScreenSourceOption) {
        _viewModel = StateObject(wrappedValue: HomeViewModel(source: source))
    }

    var body: some View {
        if viewModel.isLoading {
            ProgressView("Loading…")
        } else if let error = viewModel.error {
            Text(error)
            Button("Retry") { viewModel.retry() }
        } else if let reader = viewModel.node {
            SduiNodeView(reader: reader)
        }
    }
}
```

---

## AppKoin

Ponto de entrada Koin exposto ao iOS — inicia o mesmo grafo de dependências (`networkModule` +
`dataModule`, ambos em `commonMain`) que o Android inicia em `App.kt`. Precisa ser chamado uma
única vez, antes do primeiro `SduiSdk()` — na prática, no `init` do `@main App`:

```swift
struct RootView: View {
    @State private var source: ScreenSourceOption? = nil

    var body: some View {
        if let source {
            HomeView(source: source)
        } else {
            LauncherView(onSourceSelected: { source = $0 })
        }
    }
}

@main
struct iOSApp: App {
    init() {
        AppKoin.shared.start()
    }
    var body: some Scene {
        WindowGroup { RootView().sduiTheme() }
    }
}
```

Chamadas repetidas a `start()` são no-op (seguro em previews/testes). Para uma baseUrl
customizada (staging, testes), use o overload:

```swift
AppKoin.shared.start(baseUrl: "https://minha-api.com/screens")
```

---

## SduiSdk

Entry point do framework KMP exposto ao iOS. `fetchScreen` resolve `FetchScreenUseCase` do
grafo Koin iniciado por `AppKoin` — chamar antes de `AppKoin.shared.start()` lança uma
exceção imediatamente. `parseScreen` não usa Koin nem faz I/O — é só parsing puro.

```swift
let sdk = SduiSdk()

// Backend HTTP — busca em Kotlin (Ktor)
let reader = try await sdk.fetchScreen(route: "/home")
// → GET http://localhost:3000/screens/home (ou a baseUrl passada a AppKoin.shared.start(baseUrl:))

// Firebase Remote Config — busca em Swift (Firebase iOS SDK nativo), Kotlin só parseia
let json = RemoteConfig.remoteConfig().configValue(forKey: "home").stringValue
let reader = try sdk.parseScreen(json: json)
```

`parseScreen` não é `suspend`/`async` — é síncrono (`decodeFromString` + `NodeMapper`, sem
rede). Ver o setup completo do Firebase (projeto, `GoogleService-Info.plist`, SPM) em
[`docs/firebase.md`](firebase.md).

> **Nota:** parâmetros com valores default do Kotlin **não são exportados** para Swift. Por isso
> `AppKoin` expõe dois métodos (`start()` e `start(baseUrl:)`) em vez de um único com valor
> default.

---

## NodeReader

Wrapper Swift-friendly para leitura de props de um `Node` sem lidar com `JsonElement` diretamente.

```swift
let reader: NodeReader  // retornado por sdk.fetchScreen()

// Leitura de props
reader.type                          // "text"
reader.stringProp(key: "text")       // "Hello SDUI"
reader.doubleProp(key: "fontSize")   // KotlinDouble?(22.0) → use .doubleValue
reader.boolProp(key: "disabled")     // KotlinBool?
reader.objectProp(key: "style")      // NodeReader? do objeto aninhado
reader.children                      // [NodeReader]
```

> **Atenção:** `doubleProp` e `boolProp` retornam tipos boxed do Kotlin (`KotlinDouble?`, `KotlinBool?`). Use `.doubleValue` / `.boolValue` para converter para tipos Swift nativos:

```swift
let size = CGFloat(reader.doubleProp(key: "fontSize")?.doubleValue ?? 16)
```

---

## Renderização SDUI

O `SduiNodeView` é o renderizador recursivo — equivalente ao `RendererRegistry` do Android.

```swift
struct SduiNodeView: View {
    let reader: NodeReader

    var body: some View {
        switch reader.type {
        case "text":   SduiTextView(reader: reader)
        case "column": VStack { /* children recursivos */ }
        case "row":    HStack { /* children recursivos */ }
        default:       EmptyView()   // tipo desconhecido → ignora, sem crash
        }
    }
}
```

### Leitura de estilo aninhado

O JSON pode ter estilo e padding aninhados:

```json
{
  "type": "text",
  "props": {
    "text": "Hello SDUI",
    "style": {
      "color": "#1A202C",
      "fontSize": 22,
      "fontWeight": "semi-bold",
      "padding": { "start": 24, "end": 24, "top": 32, "bottom": 0 }
    }
  }
}
```

Leitura em Swift:

```swift
let style   = reader.objectProp(key: "style")
let padding = style?.objectProp(key: "padding")

let color      = style?.stringProp(key: "color")        // "#1A202C"
let fontSize   = style?.doubleProp(key: "fontSize")?.doubleValue ?? 16
let fontWeight = style?.stringProp(key: "fontWeight")   // "semi-bold"
let top        = padding?.doubleProp(key: "top")?.doubleValue ?? 0
```

---

## Tema (Design Tokens)

Os tokens de cor e espaçamento são definidos em Kotlin (`shared`) e lidos em Swift via `SduiColorTokens`, `SduiSpacingTokens` e `SduiRadiusTokens`.

```swift
// Objetos Kotlin são acessados via .shared (singleton)
Color(SduiColorTokens.Light.shared.primary)
CGFloat(SduiSpacingTokens.shared.spacing16)
CGFloat(SduiRadiusTokens.shared.medium)
```

O `SduiTheme` injeta os tokens no environment SwiftUI:

```swift
// Na raiz do app
HomeView()
    .sduiTheme()

// Em qualquer View filha
@Environment(\.sduiColors)  private var colors
@Environment(\.sduiSpacing) private var spacing
@Environment(\.sduiRadius)  private var radius

Text("Olá").foregroundStyle(colors.primary)
```

---

## Como adicionar um novo componente

### 1. Adicionar o case no `SduiNodeView.swift`

```swift
case "button":
    SduiButtonView(reader: reader)
```

### 2. Criar o componente SwiftUI

```swift
struct SduiButtonView: View {
    let reader: NodeReader

    private var label: String { reader.stringProp(key: "label") ?? "" }
    private var style: NodeReader? { reader.objectProp(key: "style") }
    private var color: String? { style?.stringProp(key: "color") }

    var body: some View {
        Button(label) { }
            .foregroundStyle(color.flatMap { Color(hex: $0) } ?? .primary)
    }
}
```

> Não é necessário nenhum registro explícito (sem equivalente ao `getAll()` do Koin) — basta adicionar o `case` no `switch`.

---

## Xcode — Integração com Gradle

O framework é compilado pelo Gradle e copiado para o Xcode via script de build:

```
KMM Framework Build (script Xcode)
    │
    └── ./gradlew :shared:embedAndSignAppleFrameworkForXcode
            │
            ├── compileKotlinIosSimulatorArm64
            ├── linkDebugFrameworkIosSimulatorArm64
            └── → shared/build/xcode-frameworks/Debug/iphonesimulator*/Shared.framework
```

O script inclui um guard para evitar builds múltiplos:

```sh
if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
  echo "Skipping Gradle build: OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED is YES"
  exit 0
fi
cd "$SRCROOT/.."
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

### Configurar o Xcode corretamente

```bash
sudo xcode-select -s /Applications/Xcode.app/Contents/Developer
```

> Sem isso, o linker (`xcrun`) falha e o framework não é atualizado.

---

## Como rodar localmente

```bash
# 1. Inicie o mock server
cd android-sdui-mock-server && npm start

# 2. Compile o framework KMP
./gradlew :shared:embedAndSignAppleFrameworkForXcode

# 3. Abra no Xcode e rode no simulador
open iosApp/iosApp.xcodeproj
```

O simulador iOS acessa o servidor em `http://localhost:3000` diretamente (sem necessidade de IP especial como o `10.0.2.2` do emulador Android).

---

[← Índice](README.md)
