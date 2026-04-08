plugins {
    id("convention.android.library.compose")
    id("convention.jacoco")
}

androidCompose(namespace = "com.douglassantana.sdui_components")

android {
    testOptions {
        unitTests { isReturnDefaultValues = true }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:sdui-core"))
    implementation(project(":core:sdui-runtime"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.serialization.json)
}
