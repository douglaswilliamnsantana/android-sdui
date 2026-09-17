plugins {
    id("convention.android.library.compose")
    id("convention.jacoco")
}

androidCompose(namespace = "com.douglassantana.launcher")

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
}
