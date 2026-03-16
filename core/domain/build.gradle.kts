plugins {
    id("convention.android.library")
}

android(namespace = "com.douglassantana.domain")

dependencies {
    implementation(project(":core:sdui-core"))

    implementation(libs.kotlinx.serialization.json)
}