plugins {
    id("convention.kmp.library")
}

android(namespace = "com.douglassantana.model")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:sdui-core"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
