plugins {
    id("convention.android.library.compose")
}

androidCompose(namespace = "com.douglassantana.sdui_components")

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:sdui-core"))
    implementation(project(":core:sdui-runtime"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}