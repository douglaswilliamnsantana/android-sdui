plugins {
    id("io.github.douglaswilliamnsantana.convention.kmp.library")
}

android(namespace = "com.douglassantana.core")

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.javax.inject)
}