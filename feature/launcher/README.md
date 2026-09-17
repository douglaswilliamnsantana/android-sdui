# feature:launcher

Módulo de feature responsável pela tela de seleção da fonte de dados da tela SDUI: **Backend** (HTTP, via mock server) ou **Firebase Remote Config** — ver [Firebase Remote Config](#firebase-remote-config) abaixo.

---

## Responsabilidades

- Exibir duas opções ao usuário
- Emitir a escolha como um `ScreenSource` (`core:domain`) via callback — sem gerenciar navegação, DI ou estado próprio

---

## Estrutura

```
feature/launcher/
└── src/main/kotlin/com/douglassantana/launcher/
    └── LauncherScreen.kt  → composable stateless, sem ViewModel
```

Não há módulo Koin nem ViewModel: `LauncherScreen` é puramente apresentacional. Quem chama (`app`/`MainActivity`) decide o que fazer com a escolha.

---

## LauncherScreen

```kotlin
@Composable
fun LauncherScreen(
    onSourceSelected: (ScreenSource) -> Unit,
)
```

Usado por `MainActivity` para alternar para `HomeScreen(source = ...)` (`feature:home`) assim que o usuário escolhe uma opção.

---

## Firebase Remote Config

`ScreenSource.RemoteConfig` usa o Firebase real — Firebase Android SDK no Android,
Firebase iOS SDK (nativo, via SPM) no iOS. Setup completo, convenção de chave e como
publicar dados de teste: [`docs/firebase.md`](../../docs/firebase.md).

`feature:launcher` continua sem saber de nenhum detalhe disso — só emite `ScreenSource`, a
resolução real acontece na camada de DI (`core:data`).

---

## Dependências

```kotlin
// feature/launcher/build.gradle.kts
dependencies {
    implementation(project(":core:domain")) // ScreenSource
    implementation(libs.androidx.core.ktx)
}
```

Sem dependência de `core:designsystem` — o tema (`AndroidSduiTheme`) já vem de quem compõe `LauncherScreen` (`MainActivity`), mesmo padrão usado por `feature:home`.

---

[← README do projeto](../../README.md) · [feature:home](../home/README.md)
