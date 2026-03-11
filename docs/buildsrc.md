[← Índice](README.md) · [README do projeto](../README.md)

---

# buildSrc — Convention Plugins

Módulo Gradle especial que centraliza toda a configuração de build do projeto. É compilado antes de qualquer outro módulo e seus plugins ficam disponíveis automaticamente para todos.

---

## Por que buildSrc?

Sem `buildSrc`, cada módulo precisaria repetir versões, flags de compilação e configurações de Compose. Com `buildSrc`:

- Versões definidas em um único lugar (`AppConfig.kt`)
- Cada módulo declara apenas o plugin que precisa e seu namespace
- Adicionar um novo módulo leva menos de 5 linhas
- Mudanças de versão do SDK ou JVM afetam todos os módulos de uma vez

---

## Estrutura

```
buildSrc/
├── build.gradle.kts              → kotlin-dsl + dependências dos plugins
├── settings.gradle.kts           → repositórios do buildSrc
└── src/main/kotlin/
    ├── AppConfig.kt                         → constantes globais de build
    ├── AndroidExtensions.kt                 → extensões android() / androidCompose()
    ├── AndroidApplicationConventionPlugin.kt → plugin para o módulo :app
    ├── AndroidLibraryConventionPlugin.kt     → plugin para módulos Android sem Compose
    ├── AndroidLibraryComposeConventionPlugin.kt → plugin para módulos Android com Compose
    └── KotlinLibraryConventionPlugin.kt      → plugin para módulos Kotlin puro
```

---

## Diagrama de plugins

![Diagrama de Plugins — buildSrc](images/diagram_architecture.png)

---

## `AppConfig.kt`

Centraliza todas as constantes de build. Alterar aqui propaga para todos os módulos automaticamente.

```kotlin
object AppConfig {
    const val applicationId = "com.douglassantana.android_sdui"
    const val compileSdk    = 36
    const val targetSdk     = 36
    const val minSdk        = 31
    const val versionCode   = 1
    const val versionName   = "1.0"
    const val jvmTarget     = "11"
    const val javaVersion   = "VERSION_11"
}
```

---

## `AndroidExtensions.kt`

Funções de extensão em `Project` que permitem configurar namespace (e opcionalmente Compose) com uma única chamada nos `build.gradle.kts` dos módulos.

```kotlin
// Para módulos sem Compose
fun Project.android(namespace: String) {
    extensions.configure<LibraryExtension> {
        this.namespace = namespace
    }
}

// Para módulos com Compose — injeta todas as dependências automaticamente
fun Project.androidCompose(namespace: String) {
    extensions.configure<LibraryExtension> {
        this.namespace = namespace
        buildFeatures { compose = true }
    }
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
    }
}
```

Dependências Compose injetadas automaticamente por `androidCompose()`:

| Configuração | Dependência |
|---|---|
| `implementation` | `compose-bom` (platform) |
| `implementation` | `ui`, `ui-graphics`, `ui-tooling-preview`, `material3` |
| `debugImplementation` | `ui-tooling`, `ui-test-manifest` |

---

## Plugins

### `convention.android.application`

Para o módulo `:app`. Aplica AGP application, Kotlin Android, Compose, KSP e Hilt. Configura `compileSdk`, `minSdk`, `targetSdk`, `compileOptions` e `KotlinAndroidProjectExtension` com `jvmTarget`.

Uso:
```kotlin
plugins { id("convention.android.application") }

android {
    namespace = "com.douglassantana.android_sdui"
    defaultConfig { applicationId = "com.douglassantana.android_sdui" }
}
```

---

### `convention.android.library`

Para módulos Android sem Compose. Aplica AGP library e Kotlin Android. Configura `compileSdk`, `minSdk`, `compileOptions` e `jvmTarget`.

Uso:
```kotlin
plugins { id("convention.android.library") }
android("com.douglassantana.sdui_core")
```

---

### `convention.android.library.compose`

Para módulos Android com Compose. Aplica AGP library, Kotlin Android e Kotlin Compose plugin. Configura tudo que `convention.android.library` faz, mais `buildFeatures { compose = true }`.

Uso — `androidCompose()` injeta todas as dependências Compose automaticamente:
```kotlin
plugins { id("convention.android.library.compose") }
androidCompose("com.douglassantana.sdui_runtime")
```

---

### `convention.kotlin.library`

Para módulos Kotlin puro (sem Android). Aplica apenas `kotlin-jvm` e configura `JavaPluginExtension` e `KotlinJvmProjectExtension` com `jvmTarget`.

Uso:
```kotlin
plugins { id("convention.kotlin.library") }
```

---

## Stack de build

| Ferramenta | Versão |
|---|---|
| Android Gradle Plugin | 9.1.0 |
| Kotlin | 2.3.10 |
| Gradle | 9.3.1 |
| KSP | 2.3.6 |
| Hilt Gradle Plugin | 2.59.2 |

### Notas de compatibilidade

O projeto usa `android.builtInKotlin=false` e `android.newDsl=false` no `gradle.properties` para manter compatibilidade entre AGP 9.x e KSP/Hilt, que ainda não suportam o Kotlin built-in do AGP 9. Estas flags podem ser removidas quando KSP e Hilt adicionarem suporte oficial.

---

[← Índice](README.md)
