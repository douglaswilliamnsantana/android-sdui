# Android SDUI

> **Server Driven UI** engine para Android — a interface é definida pelo servidor em JSON, desserializada, mapeada para um modelo tipado e renderizada com Jetpack Compose via injeção Hilt.

---

## Visão geral

Neste projeto, o servidor dita **o quê** mostrar (estrutura e props); o app decide **como** mostrar (layout e estilo). Nenhum deploy novo é necessário para mudar a interface — basta atualizar a resposta da API.

O pipeline completo:

```
Servidor (JSON)
      │
      ▼
   NodeDto        ← desserializado via kotlinx.serialization
      │
      ▼
    Node           ← props mantidas como JsonObject (sem perda de tipo)
      │
      ▼
ComponentRegistry  ← resolve a ComponentFactory pelo type do Node
      │
      ▼
  UIComponent      ← modelo tipado e pronto para renderização
      │
      ▼
RendererRegistry   ← resolve o ComponentRenderer pelo tipo do UIComponent
      │
      ▼
  Composable       ← UI desenhada na tela
```

---

## Mock Server

Para desenvolvimento local, utilize o mock server oficial do projeto:

**[android-sdui-mock-server](https://github.com/douglaswilliamnsantana/android-sdui-mock-server)**

O servidor expõe os endpoints que o app consome via Ktor. O emulador acessa o host pela URL `http://10.0.2.2:3000`.

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.3.10 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.59.2 (multibindings) |
| HTTP | Ktor 3.1.3 (OkHttp engine) |
| Serialização | kotlinx.serialization 1.8.1 |
| Build | AGP 9.1.0 + Gradle Kotlin DSL + KSP 2.3.6 |
| Java target | 11 |
| Min SDK | 31 |
| Compile / Target SDK | 36 |

---

## Visão da arquitetura

![Arquitetura Geral](/docs/images/diagram_architecture.png)

---

## Estrutura de módulos

```
androidsdui/
├── app/                        → entry point, MainActivity, App
│
├── feature/
│   └── home/                   → HomeScreen + HomeViewModel
│
├── core/
│   ├── model/                  → NodeDto, IStyle, IMargin (DTOs puros)
│   ├── domain/                 → SduiRepository (interface), FetchScreenUseCase, NodeMapper
│   ├── data/                   → SduiRepositoryImpl, DataModule
│   ├── network/                → HttpClient (Ktor), NetworkModule, baseUrl
│   ├── sdui-core/              → contratos: Node, UIComponent, ComponentFactory, ComponentRegistry
│   ├── sdui-runtime/           → RendererRegistry, ComponentRenderer, SduiBindingsModule
│   ├── sdui-components/        → implementações: SduiText, SduiTextFactory, SduiTextRenderer
│   └── designsystem/           → tokens de design, cores, tipografia, tema
│
└── buildSrc/                   → convention plugins e configuração centralizada
```

### Dependências entre módulos

```
                    ┌──────────────────────────────────┐
                    │               app                │
                    └──────────────────┬───────────────┘
         ┌──────────┬──────────────────┼───────────────────────┐
         ▼          ▼                  ▼                       ▼
   feature:home  core:data      core:sdui-components    core:designsystem
         │          │   └──→ core:network        │
         │          │              │              ▼
         │          ▼              ▼         core:sdui-runtime
         │      core:domain    core:model        │
         │          │              │             ▼
         └──────────┴──────────────┴────→  core:sdui-core
```

**Regras de dependência:**
- `core:model` — sem dependências de negócio; apenas DTOs e serialização
- `core:domain` — depende de `core:model` e `core:sdui-core`; define contratos e use cases
- `core:data` — implementa contratos de `core:domain`; depende de `core:network`
- `core:network` — infraestrutura HTTP pura; depende apenas de `core:model`
- `core:sdui-core` — agnóstico de UI; pode ser reutilizado em qualquer plataforma Kotlin
- `core:sdui-runtime` — conhece Compose, não conhece features
- `core:sdui-components` — implementações concretas de componentes
- `feature:home` — conhece `core:domain` e `core:sdui-core`; ignora camadas de infra

---

## Documentação por módulo

📚 **[→ Índice completo de documentação](docs/README.md)**

| Módulo | Descrição |
|---|---|
| [sdui-core](docs/sdui-core.md) | Contratos, modelos de dados e registros de factories |
| [sdui-runtime](docs/sdui-runtime.md) | Renderização Compose e registro de renderers |
| [domain](docs/domain.md) | SduiRepository, FetchScreenUseCase, NodeMapper |
| [app](docs/app.md) | Entry point e fluxo completo |
| [buildSrc](docs/buildsrc.md) | Convention plugins, AppConfig e extensões Gradle |
| [Arquitetura geral](docs/architecture.md) | Fluxo completo e diagramas de todos os módulos |

---

## Como rodar localmente

1. Clone e inicie o mock server:

```bash
git clone https://github.com/douglaswilliamnsantana/android-sdui-mock-server
cd android-sdui-mock-server
npm install && npm start
```

2. Abra o projeto no Android Studio e rode no emulador.

> O emulador acessa o host via `10.0.2.2:3000`. O `network_security_config.xml` já permite tráfego HTTP para esse endereço em builds de debug.

---

## Como adicionar um novo componente

### 1. Criar Props, Style e UIComponent

```kotlin
// Props — desserializada diretamente do JSON
@Serializable
data class SduiButtonProps(
    @SerialName("label") val label: String = "",
    @SerialName("style") val style: SduiButtonStyle? = null,
) : IProps

// Style — herda IStyle para ter padding via contrato
@Serializable
data class SduiButtonStyle(
    @SerialName("padding")  override val padding: IMargin = IMargin(),
    @SerialName("color")    val color: String? = null,
) : IStyle()

// UIComponent — modelo tipado pronto para o renderer
data class SduiButton(
    val label: String,
    val style: SduiButtonStyle = SduiButtonStyle(),
    override val children: List<UIComponent> = emptyList(),
) : UIComponent
```

### 2. Criar a `ComponentFactory`

```kotlin
class SduiButtonFactory @Inject constructor() : ComponentFactory<SduiButtonProps> {

    override fun type() = "button"

    override fun parseProps(node: Node): SduiButtonProps =
        SduiJson.decodeFromJsonElement(node.props)

    override fun create(
        props: SduiButtonProps,
        context: SDUIContext,
        children: List<UIComponent>,
    ) = SduiButton(
        label    = props.label,
        style    = props.style ?: SduiButtonStyle(),
        children = children,
    )
}
```

### 3. Criar o `ComponentRenderer`

```kotlin
class SduiButtonRenderer @Inject constructor() : ComponentRenderer<SduiButton> {

    override val type = SduiButton::class

    @Composable
    override fun Render(component: SduiButton) {
        Button(onClick = { }) {
            Text(text = component.label)
        }
    }
}
```

### 4. Registrar no módulo Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class SduiButtonModule {

    @Binds @IntoSet
    abstract fun bindFactory(factory: SduiButtonFactory): ComponentFactory<out IProps>

    @Binds @IntoSet
    abstract fun bindRenderer(renderer: SduiButtonRenderer): ComponentRenderer<*>
}
```

> Certifique-se de que `core:sdui-components` (ou o módulo onde o componente vive) está declarado como dependência direta do `app/build.gradle.kts` — Hilt precisa enxergar os módulos de DI no classpath do app.

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
| `type` | `String` | Identificador do componente. Deve corresponder ao retorno de `ComponentFactory.type()` |
| `props` | `Object` | Propriedades do componente. Desserializadas via `decodeFromJsonElement` |
| `components` | `Array` | Filhos do componente. Processados recursivamente pelo `ComponentRegistry` |

---

## Tratamento de erros

Tipos sem factory ou renderer registrados **não crasham o app** — emitem um aviso via `Log.w` e são silenciosamente ignorados:

```
W/ComponentRegistry: No factory registered for type 'carousel'. Falling back to UnknownComponent.
W/RendererRegistry: No renderer registered for type 'UnknownComponent'. Nothing will be rendered.
```

---

## buildSrc — Convention Plugins

```kotlin
// módulo sem Compose
plugins { id("convention.android.library") }
android("com.douglassantana.sdui_core")

// módulo com Compose
plugins { id("convention.android.library.compose") }
androidCompose("com.douglassantana.sdui_components")
```

| Plugin | Inclui |
|---|---|
| `convention.android.library` | Android Library + Kotlin + Serialization + KSP |
| `convention.android.library.compose` | Android Library + Kotlin + Compose + Serialization + KSP |
| `convention.android.application` | Android Application + Kotlin + Compose + KSP + Hilt |

---

## SduiJson

Instância compartilhada de `Json` em `core:sdui-core`, usada por todas as factories:

```kotlin
val SduiJson: Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
```

```kotlin
import com.douglassantana.sdui_core.factory.SduiJson
```
