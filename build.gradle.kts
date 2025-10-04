plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom("${rootDir}/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        allRules = false

        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "17"
        exclude("**/build/**")
    }

    tasks.register("detektAll") {
        group = "verification"
        description = "Runs Detekt on all subprojects"

        dependsOn(
            rootProject.subprojects.flatMap { sub ->
                sub.tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().toList()
            }
        )
    }
}