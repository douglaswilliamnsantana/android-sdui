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
    Node          ← modelo de dados bruto: type, props, children
      │
      ▼
ComponentRegistry ← resolve a ComponentFactory pelo type do Node
      │
      ▼
  UIComponent     ← modelo tipado e pronto para renderização
      │
      ▼
RendererRegistry  ← resolve o ComponentRenderer pelo tipo do UIComponent
      │
      ▼
  Composable      ← UI desenhada na tela
```

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.3.10 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt 2.59.2 (multibindings) |
| Build | AGP 9.1.0 + Gradle 9.3.1 + KSP 2.3.6 |
| Java target | 11 |
| Min SDK | 31 |
| Compile / Target SDK | 36 |

---

## Visão da arquitetura

![Arquitetura Geral](docs/images/diagram_architecture.png)

---

## Estrutura de módulos

```
androidsdui/
├── app/                        → módulo de aplicação (entry point)
│   ├── app/                    → App.kt, MainActivity.kt
│   ├── designsystem/           → tema, cores, tipografia
│   └── feature/home/           → componente HomeText (factory + renderer + DI)
│
├── core/
│   ├── sdui-core/              → contratos e modelos agnósticos de UI
│   └── sdui-runtime/           → renderização em Compose
│
└── buildSrc/                   → convention plugins e extensões Gradle
```

### Dependências entre módulos

```
         ┌─────────────┐
         │     app     │
         └──────┬──────┘
        ┌───────┴────────┐
        ▼                ▼
  ┌──────────┐   ┌──────────────┐
  │sdui-core │◄──│ sdui-runtime │
  └──────────┘   └──────────────┘
```

`sdui-core` não conhece Compose — pode ser reutilizado em qualquer plataforma Kotlin.  
`sdui-runtime` conhece Compose, mas não conhece as features.  
`app` conhece tudo, e é onde os componentes concretos são registrados via Hilt.

---

## Documentação por módulo

📚 **[→ Índice completo de documentação](docs/README.md)**

| Módulo | Descrição |
|---|---|
| [sdui-core](docs/sdui-core.md) | Contratos, modelos de dados e registros de factories |
| [sdui-runtime](docs/sdui-runtime.md) | Renderização Compose e registro de renderers |
| [app](docs/app.md) | Entry point, feature Home e Design System |
| [buildSrc](docs/buildsrc.md) | Convention plugins, AppConfig e extensões Gradle |
| [Arquitetura geral](docs/architecture.md) | Fluxo completo e diagramas de todos os módulos |

---

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
        val route = node.props["navigate"] as? String
        return HomeButton(
            label = node.props["label"] as? String ?: "",
            action = route?.let { UIAction.Navigate(it) },
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

Pronto — sem alterar nenhuma outra classe existente.

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
// módulo sem Compose
plugins { id("convention.android.library") }
android("com.douglassantana.sdui_core")

// módulo com Compose — dependências Compose injetadas automaticamente
plugins { id("convention.android.library.compose") }
androidCompose("com.douglassantana.sdui_runtime")
```

Veja [buildSrc](docs/buildsrc.md) para detalhes completos.
