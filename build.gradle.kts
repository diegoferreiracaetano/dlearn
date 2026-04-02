plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.googleServices) apply false
}

val detektVersion = libs.versions.detekt.get()

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
    }

    // Configuração mínima para o KtLint funcionar em todos os módulos
    ktlint {
        android.set(true)
        ignoreFailures.set(true)
    }

    detekt {
        config.setFrom("${rootDir}/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
        allRules = false
        parallel = true
        autoCorrect = project.hasProperty("detekt.autoCorrect")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "17"
        exclude("**/build/**")
        exclude("**/resources/**")
        exclude("**/generated/**")

        val taskName = name.lowercase()
        val baselineFile = when {
            taskName.contains("main") -> file("${rootDir}/config/detekt/baseline-${project.name}-main.xml")
            taskName.contains("test") -> file("${rootDir}/config/detekt/baseline-${project.name}-test.xml")
            taskName.contains("debug") -> file("${rootDir}/config/detekt/baseline-${project.name}-debug.xml")
            taskName.contains("release") -> file("${rootDir}/config/detekt/baseline-${project.name}-release.xml")
            else -> file("${rootDir}/config/detekt/baseline-${project.name}.xml")
        }
        
        baseline.set(baselineFile)

        reports {
            html.required = true
            xml.required = true
            txt.required = false
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "17"
        val taskName = name.lowercase()
        val baselineFile = when {
            taskName.contains("main") -> file("${rootDir}/config/detekt/baseline-${project.name}-main.xml")
            taskName.contains("test") -> file("${rootDir}/config/detekt/baseline-${project.name}-test.xml")
            taskName.contains("debug") -> file("${rootDir}/config/detekt/baseline-${project.name}-debug.xml")
            taskName.contains("release") -> file("${rootDir}/config/detekt/baseline-${project.name}-release.xml")
            else -> file("${rootDir}/config/detekt/baseline-${project.name}.xml")
        }
        baseline.set(baselineFile)
    }
}

tasks.register("detektAll") {
    group = "verification"
    description = "Runs Detekt on all subprojects"

    dependsOn(subprojects.map { sub ->
        sub.tasks.withType<io.gitlab.arturbosch.detekt.Detekt>()
    })
}

tasks.register("detektBaselineAll") {
    group = "verification"
    description = "Generates Detekt baseline for all subprojects"

    dependsOn(subprojects.map { sub ->
        sub.tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>()
    })
}

tasks.register("ktlintFormatAll") {
    group = "formatting"
    description = "Runs ktlintFormat on all subprojects"
    dependsOn(subprojects.map { it.tasks.matching { t -> t.name == "ktlintFormat" } })
}

tasks.register("ktlintCheckAll") {
    group = "verification"
    description = "Runs ktlintCheck on all subprojects"
    dependsOn(subprojects.map { it.tasks.matching { t -> t.name == "ktlintCheck" } })
}
