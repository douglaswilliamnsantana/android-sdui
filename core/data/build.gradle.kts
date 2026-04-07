plugins {
    id("convention.kmp.library")
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
    }
}

dependencies {
    kspAndroid(libs.hilt.android.compiler)
}
