plugins {
    id("convention.android.library.compose")
}

androidCompose(namespace = "com.douglassantana.sdui_runtime")

dependencies {
    implementation(project(":core:sdui-core"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)
}