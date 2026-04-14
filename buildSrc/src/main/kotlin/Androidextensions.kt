import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configura o namespace do módulo Android Library.
 * Uso para módulos sem Compose:
 * ```kotlin
 * plugins { id("convention.android.library") }
 * android(namespace = "com.douglassantana.meu_modulo")
 * ```
 */
fun Project.android(namespace: String) {
    pluginManager.withPlugin("com.android.library") {
        extensions.configure<LibraryExtension> {
            this.namespace = namespace
        }
    }
    pluginManager.withPlugin("com.android.application") {
        extensions.configure<BaseAppModuleExtension> {
            this.namespace = namespace
        }
    }
}

/**
 * Configura o namespace, habilita o Compose e adiciona automaticamente
 * todas as dependências Compose padrão do projeto.
 *
 * Dependências adicionadas:
 * - compose-bom (platform)
 * - ui, ui-graphics, ui-tooling-preview, material3  → implementation
 * - ui-tooling, ui-test-manifest                    → debugImplementation
 *
 * Uso:
 * ```kotlin
 * plugins { id("convention.android.library.compose") }
 * androidCompose(namespace = "com.douglassantana.meu_modulo")
 * ```
 */
fun Project.androidCompose(namespace: String) {
    pluginManager.withPlugin("com.android.library") {
        extensions.configure<LibraryExtension> {
            this.namespace = namespace
            buildFeatures { compose = true }
        }
    }
    pluginManager.withPlugin("com.android.application") {
        extensions.configure<BaseAppModuleExtension> {
            this.namespace = namespace
            buildFeatures { compose = true }
        }
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