plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:9.1.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.3.10")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.3.6")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
    implementation("org.jetbrains.kotlin:kotlin-serialization:2.3.10")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "convention.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "convention.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("kmpLibrary") {
            id = "convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("jacoco") {
            id = "convention.jacoco"
            implementationClass = "JacocoConventionPlugin"
        }
    }
}