plugins {
    id("convention.kmp.library")
    id("convention.jacoco")
}

android(namespace = "com.douglassantana.data")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:model"))
            implementation(project(":core:network"))
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.firebase.config)
            implementation(libs.kotlinx.coroutines.play.services)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

dependencies {
    // BOM applied here (not inside kotlin { sourceSets { androidMain.dependencies { } } })
    // because KotlinDependencyHandler.platform() is deprecated for removal (KT-58759);
    // this uses the regular, non-deprecated Gradle DependencyHandlerScope.platform().
    "androidMainImplementation"(platform(libs.firebase.bom))
}
