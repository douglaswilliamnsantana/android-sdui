plugins {
    id("io.github.douglaswilliamnsantana.convention.android.library.compose")
}

androidCompose(namespace = "com.douglassantana.runtime")

dependencies {
    implementation(project(":sdui:core"))

    implementation(libs.compose.runtime)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)
}