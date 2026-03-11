plugins {
    id("convention.android.library.compose")
}

androidCompose(namespace = "com.douglassantana.sdui_runtime")

dependencies {
    implementation(project(":core:sdui-core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)
}