import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        compileTaskProvider.configure {
                            compilerOptions {
                                jvmTarget.set(JvmTarget.fromTarget(AppConfig.jvmTarget))
                            }
                        }
                    }
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }

            extensions.configure<LibraryExtension> {
                compileSdk = AppConfig.compileSdk
                defaultConfig {
                    minSdk = AppConfig.minSdk
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.valueOf(AppConfig.javaVersion)
                    targetCompatibility = JavaVersion.valueOf(AppConfig.javaVersion)
                }
            }
        }
    }
}
