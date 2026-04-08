// Root build — aggregates JaCoCo reports from all submodules
plugins {
    jacoco
}

val coveredModules = listOf(
    ":core:model",
    ":core:domain",
    ":core:sdui-core",
    ":core:sdui-components",
    ":core:sdui-runtime",
    ":core:data",
    ":shared",
    ":feature:home",
)

tasks.register("jacocoFullReport", JacocoReport::class) {
    group = "verification"
    description = "Aggregates JaCoCo coverage reports from all modules"

    dependsOn(coveredModules.map { "$it:jacocoReport" })

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full"))
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/full/jacocoFullReport.xml")
        )
    }

    val excludes = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*_HiltModules*", "**/*_MembersInjector*", "**/*_Factory*",
        "**/*Module_*Provides*", "**/Hilt_*", "**/hilt_aggregated_deps/**",
        "**/*\$serializer*", "**/*\$DefaultImpls*",
        "**/*Test*", "**/test/**",
    )

    sourceDirectories.setFrom(
        coveredModules.flatMap { path ->
            val proj = project(path)
            listOf(
                proj.file("src/main/kotlin"),
                proj.file("src/commonMain/kotlin"),
                proj.file("src/androidMain/kotlin"),
            ).filter { it.exists() }
        }
    )

    classDirectories.setFrom(
        coveredModules.map { path ->
            val proj = project(path)
            proj.fileTree(
                proj.layout.buildDirectory
                    .dir("intermediates/runtime_library_classes_dir/debug/bundleLibRuntimeToDirDebug")
                    .get()
            ) { exclude(excludes) }
        }
    )

    executionData.setFrom(
        coveredModules.map { path ->
            project(path).layout.buildDirectory
                .file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
                .get()
        }
    )
}
