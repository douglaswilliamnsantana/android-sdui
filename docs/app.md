[← Índice](README.md) · [README do projeto](../README.md)

---

# Módulo: app

Módulo de aplicação — entry point do projeto. Contém a inicialização do Hilt, o Design System, e a feature `home` com o primeiro componente SDUI concreto do projeto.

---

## Estrutura interna

```
app/
├── app/
│   ├── App.kt              → Application com @HiltAndroidApp
│   └── MainActivity.kt     → monta o grafo, cria o Node raiz, renderiza
│
├── designsystem/
│   └── theme/
│       ├── Color.kt        → paleta de cores do Material 3
│       ├── Type.kt         → escala tipográfica
│       └── Theme.kt        → AndroidSduiTheme
│
└── feature/
    └── home/
        ├── component/
        │   └── HomeText.kt         → UIComponent concreto
        ├── factory/
        │   └── HomeTextFactory.kt  → cria HomeText a partir do Node
        ├── renderer/
        │   └── HomeTextRenderer.kt → desenha HomeText em Compose
        └── di/
            └── HomeSDUIModule.kt   → registra factory e renderer via Hilt
```

---

## Diagrama de classes

![Diagrama de Classes — app](images/diagram_classes.png)

---

## App.kt

Ponto de inicialização do Hilt. A anotação `@HiltAndroidApp` dispara a geração de código KSP e inicializa o grafo de dependências global. Deve ser declarada no `AndroidManifest.xml`:

```xml
<application android:name=".app.App" ...>
```

```kotlin
@HiltAndroidApp
class App : Application()
```

---

## MainActivity.kt

Recebe via `@Inject` o `ComponentRegistry` e o `RendererRegistry` — os dois registros centrais do sistema. Em `setContent`, cria o `Node` raiz (que em produção viria de uma API remota) e renderiza o componente resultante dentro do tema.

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var componentRegistry: ComponentRegistry
    @Inject lateinit var rendererRegistry: RendererRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val component = remember {
                val node = Node(type = "text", props = mapOf("text" to "SDUI com Hilt"))
                componentRegistry.create(node, SDUIContext())
            }
            AndroidSduiTheme {
                rendererRegistry.Render(component)
            }
        }
    }
}
```

O `remember {}` garante que `componentRegistry.create` seja chamado apenas uma vez por composição — não a cada recomposição.

---

## Feature: Home

### `HomeText`

`UIComponent` concreto que representa um texto simples na tela. Criado pela `HomeTextFactory` a partir de um `Node` do tipo `"text"`.

```kotlin
data class HomeText(
    val value: String,
    override val children: List<UIComponent> = emptyList()
) : UIComponent
```

---

### `HomeTextFactory`

Lê a prop `"text"` do `Node` e instancia o `HomeText`. Se a prop estiver ausente, usa string vazia como fallback — sem crashar.

```kotlin
class HomeTextFactory @Inject constructor() : ComponentFactory {
    override fun type() = "text"

    override fun create(node: Node, context: SDUIContext, children: List<UIComponent>): UIComponent {
        return HomeText(
            value = node.props["text"] as? String ?: "",
            children = children
        )
    }
}
```

---

### `HomeTextRenderer`

Desenha o `HomeText` como um `Text` do Material 3. Os filhos são renderizados automaticamente pelo `RendererRegistry` após `Render` — não é necessário tratá-los aqui.

```kotlin
class HomeTextRenderer @Inject constructor() : ComponentRenderer<HomeText> {
    override val type = HomeText::class

    @Composable
    override fun Render(component: HomeText) {
        Text(text = component.value)
    }
}
```

---

### `HomeSDUIModule`

Módulo Hilt que conecta as implementações concretas aos contratos do `sdui-core` e `sdui-runtime` via multibindings. É a única classe que precisa ser alterada ao adicionar um novo componente à feature Home.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class HomeSDUIModule {

    @Binds @IntoSet
    abstract fun bindHomeTextFactory(factory: HomeTextFactory): ComponentFactory

    @Binds @IntoSet
    abstract fun bindHomeTextRenderer(renderer: HomeTextRenderer): ComponentRenderer<*>
}
```

---

## Design System

O tema é baseado em Material 3 com paleta, tipografia e tema customizados. Envolve toda a árvore Compose via `AndroidSduiTheme { }` na `MainActivity`.

```
designsystem/theme/
├── Color.kt   → MaterialTheme.colorScheme
├── Type.kt    → MaterialTheme.typography
└── Theme.kt   → AndroidSduiTheme { content() }
```

---

## Fluxo completo na MainActivity

![Fluxo Completo na MainActivity](images/diagram_sequence.png)

---

[← Índice](README.md)
