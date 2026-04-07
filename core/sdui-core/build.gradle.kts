plugins {
    id("convention.kmp.library")
}

android(namespace = "com.douglassantana.sdui_core")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.javax.inject)
        }
    }
}
