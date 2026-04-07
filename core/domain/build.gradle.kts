plugins {
    id("convention.kmp.library")
}

android(namespace = "com.douglassantana.domain")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:sdui-core"))
            implementation(project(":core:model"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
