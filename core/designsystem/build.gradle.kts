plugins {
    id("convention.android.library.compose")
}

androidCompose(namespace = "com.douglassantana.designsystem")

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)
}