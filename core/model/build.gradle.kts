plugins {
    id("io.github.douglaswilliamnsantana.convention.android.library")
}

android(namespace = "com.douglassantana.model")

dependencies {

    implementation(libs.kotlinx.serialization.json)
}