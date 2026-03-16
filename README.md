# Android SDUI

> **Server Driven UI** engine para Android — componentes declarativos construídos a partir de dados do servidor, renderizados com Jetpack Compose e injetados via Hilt.

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

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.3.10 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.59.2 (multibindings) |
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
├── app/                        → entry point da aplicação
│   ├── App.kt                  → @HiltAndroidApp
│   ├── MainActivity.kt         → monta e renderiza o componente raiz
│   └── SduiBindingsModule.kt   → inicializa os sets vazios do Hilt multibinding
│
├── core/
│   ├── sdui-core/              → contratos e modelos agnósticos de UI
│   ├── sdui-runtime/           → renderização em Compose
│   ├── sdui-components/        → implementações de componentes (SduiText, ...)
│   ├── domain/                 → NodeDto, NodeMapper, IStyle, IMargin
│   └── designsystem/           → tokens de design, cores, tipografia, tema
│
└── buildSrc/                   → convention plugins e configuração centralizada
```

### Dependências entre módulos

```
              ┌─────────────────────────────┐
              │            app              │
              └──────────────┬──────────────┘
          ┌───────┬──────────┼──────────┬───────────┐
          ▼       ▼          ▼          ▼           ▼
     domain  designsystem sdui-core sdui-runtime sdui-components
          │                  ▲          │               │
          └──────────────────┘          ▼               ▼
                                    sdui-core       sdui-core
                                                    sdui-runtime
                                                    domain
```

- `sdui-core` não conhece Compose — pode ser reutilizado em qualquer plataforma Kotlin.
- `sdui-runtime` conhece Compose, mas não conhece as features.
- `domain` contém os modelos de dados e o mapeamento JSON → Node.
- `sdui-components` reúne as implementações concretas de componentes.
- `app` integra tudo e registra os componentes via Hilt.

---

## Documentação por módulo

📚 **[→ Índice completo de documentação](docs/README.md)**

| Módulo | Descrição |
|---|---|
| [sdui-core](docs/sdui-core.md) | Contratos, modelos de dados e registros de factories |
| [sdui-runtime](docs/sdui-runtime.md) | Renderização Compose e registro de renderers |
| [app](docs/app.md) | Entry point, SduiBindingsModule e fluxo completo |
| [buildSrc](docs/buildsrc.md) | Convention plugins, AppConfig e extensões Gradle |
| [Arquitetura geral](docs/architecture.md) | Fluxo completo e diagramas de todos os módulos |

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

    // Uma linha — sem casts, sem parsing manual
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
        Button(
            onClick = { },
            modifier = Modifier.marginResolved(component.style.padding),
        ) {
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

Pronto — sem alterar nenhuma outra classe existente.

---

## Formato do JSON

```json
{
  "type": "text",
  "props": {
    "text": "Hello SDUI",
    "style": {
      "padding": {
        "start": 24,
        "end": 24,
        "top": 32,
        "bottom": 0
      },
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
| `props` | `Object` | Propriedades do componente. Desserializadas diretamente na Props class via `decodeFromJsonElement` |
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

O projeto usa `buildSrc` para centralizar toda a configuração de build. Cada módulo declara apenas o plugin e namespace:

```kotlin
// módulo sem Compose (inclui serialization plugin automaticamente)
plugins { id("convention.android.library") }
android("com.douglassantana.sdui_core")

// módulo com Compose (inclui serialization + Compose plugins automaticamente)
plugins { id("convention.android.library.compose") }
androidCompose("com.douglassantana.sdui_components")
```

| Plugin | Inclui |
|---|---|
| `convention.android.library` | Android Library + Kotlin + Serialization |
| `convention.android.library.compose` | Android Library + Kotlin + Compose + Serialization + KSP |
| `convention.android.application` | Android Application + Kotlin + Compose + KSP + Hilt |

---

## SduiJson

Instância compartilhada de `Json` disponível em `core:sdui-core`. Usada por todas as factories e pela desserialização inicial do JSON na `MainActivity`.

```kotlin
val SduiJson: Json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
```

Importar de:
```kotlin
import com.douglassantana.sdui_core.factory.SduiJson
```
