# Android SDUI Sample

Mini projeto de **Server Driven UI (SDUI)** para Android, demonstrando como construir uma engine extensível baseada em componentes declarativos renderizados a partir de dados do servidor.

## Tecnologias

- **Kotlin** + **Jetpack Compose**
- **Hilt** para injeção de dependência
- **Clean Architecture**
- **Factory Pattern** + **Registry Pattern**

## Arquitetura

O fluxo de dados segue o pipeline abaixo:

```
Node (JSON) → ComponentRegistry → UIComponent → RendererRegistry → Composable
```

1. Um `Node` representa o contrato vindo do servidor (tipo, props, filhos).
2. O `ComponentRegistry` encontra a `ComponentFactory` correta pelo `type` e instancia o `UIComponent`, processando recursivamente os `children`.
3. O `RendererRegistry` encontra o `ComponentRenderer` correto pelo tipo do componente e delega a renderização ao Compose, renderizando também os filhos recursivamente.

## Estrutura de pacotes

```
app/
├── app/
│   ├── App.kt                  # Application com @HiltAndroidApp
│   └── MainActivity.kt         # Entry point, monta o grafo e renderiza
├── designsystem/
│   └── theme/                  # Cores, tipografia e tema Material3
├── feature/
│   └── home/
│       ├── component/          # HomeText — UIComponent concreto
│       ├── di/                 # HomeSDUIModule — bindings Hilt
│       ├── factory/            # HomeTextFactory — cria HomeText a partir do Node
│       └── renderer/           # HomeTextRenderer — renderiza HomeText em Compose
└── sduiCore/
    ├── Node.kt                 # Modelo de dados do servidor (type, props, children)
    ├── UIComponent.kt          # Interface base com suporte a children
    ├── UnknownComponent.kt     # Fallback para tipos não registrados
    ├── action/
    │   ├── UIAction.kt         # Ações seladas: Navigate, Log
    │   └── ActionHandler.kt    # Interface para tratar ações
    ├── context/
    │   └── SDUIContext.kt      # Contexto propagado às factories (actionHandler, locale, extras)
    ├── factory/
    │   └── ComponentFactory.kt # Interface: type() + create(node, context, children)
    └── registry/
        └── ComponentRegistry.kt # Resolve factories e processa árvore de nodes
sduiRuntime/
    ├── compose/
    │   └── ComponentRenderer.kt # Interface Composable genérica por tipo
    └── renderer/
        └── RendererRegistry.kt  # Resolve renderers e renderiza árvore recursivamente
```

## Como adicionar um novo componente

### 1. Criar o `UIComponent`

```kotlin
data class HomeButton(
    val label: String,
    val action: UIAction? = null,
    override val children: List<UIComponent> = emptyList()
) : UIComponent
```

### 2. Criar a `ComponentFactory`

```kotlin
class HomeButtonFactory @Inject constructor() : ComponentFactory {
    override fun type() = "button"

    override fun create(node: Node, context: SDUIContext, children: List<UIComponent>): UIComponent {
        val actionRoute = node.props["navigate"] as? String
        return HomeButton(
            label = node.props["label"] as? String ?: "",
            action = actionRoute?.let { UIAction.Navigate(it) },
            children = children
        )
    }
}
```

### 3. Criar o `ComponentRenderer`

```kotlin
class HomeButtonRenderer @Inject constructor() : ComponentRenderer<HomeButton> {
    override val type = HomeButton::class

    @Composable
    override fun Render(component: HomeButton) {
        Button(onClick = { /* dispatch component.action */ }) {
            Text(component.label)
        }
    }
}
```

### 4. Registrar no módulo Hilt

```kotlin
@Binds @IntoSet
abstract fun bindHomeButtonFactory(factory: HomeButtonFactory): ComponentFactory

@Binds @IntoSet
abstract fun bindHomeButtonRenderer(renderer: HomeButtonRenderer): ComponentRenderer<*>
```

## SDUIContext

O `SDUIContext` é propagado a todas as factories durante a criação de componentes, carregando:

| Campo | Tipo | Descrição |
|---|---|---|
| `actionHandler` | `ActionHandler?` | Dispatcher de ações (navegação, logs, etc.) |
| `locale` | `Locale` | Locale do dispositivo (padrão: `Locale.getDefault()`) |
| `extras` | `Map<String, Any?>` | Dados extras de contexto (ex: userId, flags) |

## Tratamento de erros

Tipos sem factory ou renderer registrado emitem um aviso via `Log.w` — sem crashar a aplicação:

```
W/ComponentRegistry: No factory registered for type 'carousel'. Falling back to UnknownComponent.
W/RendererRegistry: No renderer registered for type 'UnknownComponent'. Nothing will be rendered.
```
