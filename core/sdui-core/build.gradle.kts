plugins {
    id("convention.kmp.library")
    id("convention.jacoco")
}

android(namespace = "com.douglassantana.sdui_core")

android {
    testOptions {
        unitTests { isReturnDefaultValues = true }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.javax.inject)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

dependencies {
    testImplementation(libs.junit)
}
