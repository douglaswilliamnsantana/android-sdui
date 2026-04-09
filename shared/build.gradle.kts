plugins {
    id("convention.kmp.library")
    id("convention.jacoco")
    id("maven-publish")
}

android(namespace = "com.douglassantana.shared")

kotlin {
    // Configure iOS framework output for all three iOS targets
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            // Export public APIs from the KMP modules
            export(project(":core:sdui-core"))
            export(project(":core:model"))
            export(project(":core:domain"))
            export(project(":core:network"))
            export(project(":core:data"))
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:sdui-core"))
            api(project(":core:model"))
            api(project(":core:domain"))
            api(project(":core:network"))
            api(project(":core:data"))
            api(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/douglaswilliamnsantana/android-sdui")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
