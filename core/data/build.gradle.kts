plugins {
    id("convention.android.library")
}

android(namespace = "com.douglassantana.data")

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.ktor.client.core)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}