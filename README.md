# Android SDUI — KMP

> **Server Driven UI** engine multiplataforma (Android + iOS) — a interface é definida pelo servidor em JSON, desserializada, mapeada para um modelo tipado e renderizada com Jetpack Compose (Android) ou SwiftUI (iOS) via Kotlin Multiplatform.

---

## Visão geral

Neste projeto, o servidor dita **o quê** mostrar (estrutura e props); o app decide **como** mostrar (layout e estilo). Nenhum deploy novo é necessário para mudar a interface — basta atualizar a resposta da API.

O pipeline completo (shared entre plataformas):

```
Servidor (JSON)
      │
      ▼
   NodeDto        ← desserializado via kotlinx.serialization (commonMain)
      │
      ▼
    Node           ← props mantidas como JsonObject (sem perda de tipo)
      │
      ▼
 [Android]                          [iOS]
ComponentRegistry                NodeReader
      │                               │
      ▼                               ▼
  UIComponent                  SduiNodeView (SwiftUI)
      │
      ▼
RendererRegistry
      │
      ▼
  Composable
```

---

## Mock Server

Para desenvolvimento local, utilize o mock server oficial do projeto:

**[android-sdui-mock-server](https://github.com/douglaswilliamnsantana/android-sdui-mock-server)**

| Plataforma | URL base |
|---|---|
| Android Emulator | `http://10.0.2.2:3000/screens` |
| iOS Simulator | `http://localhost:3000/screens` |

---

## Stack

### Compartilhado (KMP)

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.3.10 |
| HTTP | Ktor 3.1.3 |
| Serialização | kotlinx.serialization 1.8.1 |
| Build | Gradle Kotlin DSL + KSP 2.3.6 |

### Android

| Camada | Tecnologia |
|---|---|
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.59.2 |
| HTTP engine | Ktor OkHttp |
| Min SDK | 31 · Compile SDK 36 |

### iOS

| Camada | Tecnologia |
|---|---|
| UI | SwiftUI |
| DI | Inicializador Swift (sem framework externo) |
| HTTP engine | Ktor Darwin |
| Min SDK | iOS 16.0 |
| Xcode | 15+ |

---

## Visão da arquitetura

![Arquitetura Geral](/docs/images/diagram_architecture.png)

---

## Estrutura de módulos

```
androidsdui/
│
├── app/                        → entry point Android, MainActivity, App
│
├── iosApp/                     → entry point iOS (Xcode)
│   └── iosApp/
│       ├── Home/
│       │   ├── HomeViewModel.swift   ← ObservableObject (= HomeViewModel.kt)
│       │   ├── HomeView.swift        ← SwiftUI View (= HomeScreen.kt)
│       │   └── SduiNodeView.swift    ← renderizador recursivo SDUI
│       └── Theme/
│           └── SduiTheme.swift       ← tokens de design bridged do KMP
│
├── shared/                     → framework KMP exportado ao iOS
│   └── src/commonMain/
│       └── SduiSdk.kt          ← entry point público para iOS
│       └── NodeReader.kt       ← helper Swift-friendly para leitura de props
│
├── feature/
│   └── home/                   → HomeScreen + HomeViewModel (Android)
│
├── core/
│   ├── model/                  → NodeDto, IStyle, IMargin          [KMP]
│   ├── domain/                 → SduiRepository, FetchScreenUseCase [KMP]
│   ├── data/                   → SduiRepositoryImpl, DataModule     [KMP]
│   ├── network/                → HttpClient (Ktor), NetworkModule    [KMP]
│   ├── sdui-core/              → Node, UIComponent, ComponentFactory [KMP]
│   ├── sdui-runtime/           → RendererRegistry, ComponentRenderer [Android]
│   ├── sdui-components/        → SduiText e outros componentes       [Android]
│   └── designsystem/           → tokens de design, cores, tipografia [Android]
│
└── buildSrc/                   → convention plugins e configuração centralizada
```

### Dependências entre módulos

```
            ┌──────────────────────────────────┐
            │               app                │
            └──────────────────┬───────────────┘
     ┌──────────┬──────────────┼────────────────────────┐
     ▼          ▼              ▼                        ▼
feature:home  core:data  core:sdui-components    core:designsystem
     │          │   └──→ core:network        │
     │          │              │              ▼
     │          ▼              ▼         core:sdui-runtime
     │      core:domain    core:model        │
     │          │              │             ▼
     └──────────┴──────────────┴────→  core:sdui-core
                                            ▲
                                        shared/
                                     (exporta ao iOS)
```

---

## Documentação por módulo

📚 **[→ Índice completo de documentação](docs/README.md)**

| Módulo | Descrição |
|---|---|
| [sdui-core](docs/sdui-core.md) | Contratos, modelos de dados e registros de factories |
| [sdui-runtime](docs/sdui-runtime.md) | Renderização Compose e registro de renderers |
| [domain](docs/domain.md) | SduiRepository, FetchScreenUseCase, NodeMapper |
| [app](docs/app.md) | Entry point Android e fluxo completo |
| [iOS](docs/ios.md) | Integração KMP, MVVM SwiftUI, NodeReader e renderização |
| [buildSrc](docs/buildsrc.md) | Convention plugins, AppConfig e extensões Gradle |
| [Arquitetura geral](docs/architecture.md) | Fluxo completo e diagramas de todos os módulos |

---

## Como rodar localmente

### 1. Mock Server

```bash
git clone https://github.com/douglaswilliamnsantana/android-sdui-mock-server
cd android-sdui-mock-server
npm install && npm start
```

### 2. Clonar com submodules

Este projeto usa submodules Git. Sempre clone com:

```bash
git clone --recurse-submodules https://github.com/douglaswilliamnsantana/androidsdui.git
```

Se já clonou sem a flag, inicialize os submodules manualmente:

```bash
git submodule update --init --recursive
```

| Submodule | Caminho | Repositório |
|---|---|---|
| Design System | `core/designsystem` | [designsystem](https://github.com/douglaswilliamnsantana/designsystem) |

### 3. Android

Abra o projeto no Android Studio e rode no emulador.

> O emulador acessa o host via `10.0.2.2:3000`. O `network_security_config.xml` já permite tráfego HTTP para esse endereço em builds de debug.

### 3. iOS

```bash
# 1. Garanta que o Xcode está configurado corretamente
sudo xcode-select -s /Applications/Xcode.app/Contents/Developer

# 2. Compile o framework KMP
./gradlew :shared:embedAndSignAppleFrameworkForXcode

# 3. Abra no Xcode
open iosApp/iosApp.xcodeproj
```

> O simulador iOS acessa o host via `localhost:3000` diretamente.

---

## Como adicionar um novo componente

### Android

#### 1. Criar Props, Style e UIComponent

```kotlin
@Serializable
data class SduiButtonProps(
    @SerialName("label") val label: String = "",
    @SerialName("style") val style: SduiButtonStyle? = null,
) : IProps

data class SduiButton(
    val label: String,
    val style: SduiButtonStyle = SduiButtonStyle(),
    override val children: List<UIComponent> = emptyList(),
) : UIComponent
```

#### 2. Criar `ComponentFactory` e `ComponentRenderer`

```kotlin
class SduiButtonFactory @Inject constructor() : ComponentFactory<SduiButtonProps> {
    override fun type() = "button"
    override fun parseProps(node: Node): SduiButtonProps = SduiJson.decodeFromJsonElement(node.props)
    override fun create(props: SduiButtonProps, context: SDUIContext, children: List<UIComponent>) =
        SduiButton(label = props.label, children = children)
}

class SduiButtonRenderer @Inject constructor() : ComponentRenderer<SduiButton> {
    override val type = SduiButton::class
    @Composable
    override fun Render(component: SduiButton) {
        Button(onClick = { }) { Text(text = component.label) }
    }
}
```

#### 3. Registrar no módulo Hilt

```kotlin
@Module @InstallIn(SingletonComponent::class)
abstract class SduiButtonModule {
    @Binds @IntoSet abstract fun bindFactory(f: SduiButtonFactory): ComponentFactory<out IProps>
    @Binds @IntoSet abstract fun bindRenderer(r: SduiButtonRenderer): ComponentRenderer<*>
}
```

### iOS

Adicionar o case correspondente no `SduiNodeView.swift`:

```swift
case "button":
    SduiButtonView(reader: reader)
```

Criar o componente SwiftUI:

```swift
struct SduiButtonView: View {
    let reader: NodeReader
    var body: some View {
        Button(reader.stringProp(key: "label") ?? "") { }
    }
}
```

---

## Formato do JSON

```json
{
  "type": "text",
  "props": {
    "text": "Hello SDUI",
    "style": {
      "padding": { "start": 24, "end": 24, "top": 32, "bottom": 0 },
      "color": "#1A202C",
      "fontSize": 22,
      "fontWeight": "semi-bold"
    }
  },
  "components": []
}
```

| Campo | Tipo | Descrição |
|---|---|---|
| `type` | `String` | Identificador do componente |
| `props` | `Object` | Propriedades do componente |
| `components` | `Array` | Filhos do componente (processados recursivamente) |

---

## Tratamento de erros

**Android:** tipos sem factory ou renderer registrados emitem `Log.w` e são silenciosamente ignorados via `UnknownComponent`.

**iOS:** o `SduiNodeView` trata tipos desconhecidos com `EmptyView()` — sem crash, sem renderização.

---

## buildSrc — Convention Plugins

```kotlin
// módulo KMP (Android + iOS)
plugins { id("convention.kmp.library") }

// módulo Android sem Compose
plugins { id("convention.android.library") }

// módulo Android com Compose
plugins { id("convention.android.library.compose") }
```

| Plugin | Plataformas |
|---|---|
| `convention.kmp.library` | Android + iOS (iosX64, iosArm64, iosSimulatorArm64) |
| `convention.android.library` | Android only |
| `convention.android.library.compose` | Android only + Compose |
| `convention.android.application` | Android application |
