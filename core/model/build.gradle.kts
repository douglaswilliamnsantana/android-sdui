plugins {
    id("convention.kmp.library")
    id("convention.jacoco")
}

android(namespace = "com.douglassantana.model")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:sdui-core"))
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
