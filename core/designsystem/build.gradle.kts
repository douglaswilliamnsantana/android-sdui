plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.douglassantana.designsystem"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
}

dependencies {

}