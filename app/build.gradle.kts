plugins {
    id("convention.android.application")
    alias(libs.plugins.google.services)
}

android(namespace = "com.douglassantana.android_sdui")

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:sdui-runtime"))
    implementation(project(":core:sdui-core"))
    implementation(project(":core:sdui-components"))
    implementation(project(":feature:home"))
    implementation(project(":feature:launcher"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.koin.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common)
}
