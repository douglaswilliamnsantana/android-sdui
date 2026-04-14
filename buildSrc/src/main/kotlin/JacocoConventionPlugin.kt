import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class JacocoConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jacoco")

            extensions.configure<JacocoPluginExtension> {
                toolVersion = "0.8.12"
            }

            // Enable coverage instrumentation on the debug build type
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            enableUnitTestCoverage = true
                        }
                    }
                }
            }

            tasks.register<JacocoReport>("jacocoReport") {
                group = "verification"
                description = "Generates JaCoCo coverage report for ${project.name}"
                dependsOn("testDebugUnitTest")

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    csv.required.set(false)
                }

                val excludes = listOf(
                    // Android resources
                    "**/R.class", "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    // Hilt / Dagger generated
                    "**/*_HiltModules*",
                    "**/*_MembersInjector*",
                    "**/*_Factory*",
                    "**/*Module_*Provides*",
                    "**/Hilt_*",
                    "**/hilt_aggregated_deps/**",
                    "**/dagger/hilt/**",
                    // KSP / kotlinx.serialization generated
                    "**/*$\$serializer*",
                    "**/*$\$delegate*",
                    // Kotlin internals
                    "**/*\$DefaultImpls*",
                    "**/*\$WhenMappings*",
                    // Test classes
                    "**/*Test*",
                    "**/test/**",
                )

                // Source dirs: commonMain for KMP modules, main for pure Android modules
                val srcDirs = listOf(
                    "src/main/kotlin",
                    "src/commonMain/kotlin",
                    "src/androidMain/kotlin",
                ).map { file(it) }.filter { it.exists() }

                sourceDirectories.setFrom(files(srcDirs))

                // AGP 9 places compiled library classes here for both KMP and Android modules
                classDirectories.setFrom(
                    fileTree(
                        layout.buildDirectory
                            .dir("intermediates/runtime_library_classes_dir/debug/bundleLibRuntimeToDirDebug")
                            .get()
                    ) { exclude(excludes) }
                )

                // Exec file produced by AGP when enableUnitTestCoverage = true
                executionData.setFrom(
                    layout.buildDirectory
                        .file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
                        .get()
                )
            }
        }
    }
}
