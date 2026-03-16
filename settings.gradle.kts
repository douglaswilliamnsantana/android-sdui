pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "android-sdui"
include(":app")
include(":core")
include(":core:sdui-runtime")
include(":core:sdui-core")
include(":core:designsystem")
include(":feature")
include(":core:sdui-components")
include(":core:domain")
