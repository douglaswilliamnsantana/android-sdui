plugins {
    id("convention.kmp.library")
    id("convention.jacoco")
    id("com.google.devtools.ksp")
}

android(namespace = "com.douglassantana.data")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:model"))
            implementation(project(":core:network"))
            implementation(libs.ktor.client.core)
        }
        androidMain.dependencies {
            implementation(libs.hilt.android)
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
    kspAndroid(libs.hilt.android.compiler)
}
