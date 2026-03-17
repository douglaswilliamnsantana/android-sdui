plugins {
    id("convention.android.library")
}

android(namespace = "com.douglassantana.network")

dependencies {
    implementation(project(":core:model"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}
