plugins {
    id("convention.android.library")
}

android(namespace = "com.douglassantana.model")

dependencies {
    implementation(project(":core:sdui-core"))

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}