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
            ├── SduiSdk          ← entry point: chama fetchScreen()
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
    ├── iOSApp.swift              ← @main, aplica sduiTheme()
    ├── Home/
    │   ├── HomeViewModel.swift   ← ObservableObject, chama SduiSdk
    │   ├── HomeView.swift        ← SwiftUI View, observa HomeViewModel
    │   └── SduiNodeView.swift    ← renderizador recursivo de Node
    └── Theme/
        └── SduiTheme.swift       ← bridge de tokens de design KMP → SwiftUI
```

---

## MVVM no iOS

O padrão é análogo ao Android. A tabela abaixo mostra a correspondência direta:

| Conceito | Android (Kotlin) | iOS (Swift) |
|---|---|---|
| Estado reativo | `StateFlow` | `@Published` |
| ViewModel | `@HiltViewModel` + `ViewModel` | `ObservableObject` |
| Injeção de dependência | Hilt (`@Inject`) | Inicializador Swift |
| Observar na View | `collectAsState()` | `@StateObject` |
| Lançar coroutine | `viewModelScope.launch` | `Task { await ... }` |
| Thread da UI | automático (Hilt/Compose) | `@MainActor` |
| Ciclo de vida | `init { loadScreen() }` | `init { Task { await loadScreen() } }` |

### HomeViewModel.swift

```swift
@MainActor
final class HomeViewModel: ObservableObject {

    @Published private(set) var node: NodeReader? = nil
    @Published private(set) var isLoading: Bool = true
    @Published private(set) var error: String? = nil

    private let sdk: SduiSdk

    init(sdk: SduiSdk = SduiSdk()) {
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
            node = try await sdk.fetchScreen(route: "/home")
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
```

### HomeView.swift

```swift
struct HomeView: View {
    @StateObject private var viewModel = HomeViewModel()

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

## SduiSdk

Entry point do framework KMP exposto ao iOS.

```swift
// URL default: http://localhost:3000/screens
let sdk = SduiSdk()

// URL customizada
let sdk = SduiSdk(baseUrl: "https://minha-api.com/screens")

// Chamada (suspend → async throws no Swift)
let reader = try await sdk.fetchScreen(route: "/home")
// → GET http://localhost:3000/screens/home
```

> **Nota:** parâmetros com valores default do Kotlin **não são exportados** para Swift. Por isso `SduiSdk` tem um construtor secundário explícito `constructor() : this("http://localhost:3000/screens")`.

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

> Não é necessário nenhum registro explícito (sem equivalente ao Hilt multibindings) — basta adicionar o `case` no `switch`.

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
