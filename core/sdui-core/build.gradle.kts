plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.douglassantana.sdui_core"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
}

dependencies {
    implementation(libs.javax.inject)
}