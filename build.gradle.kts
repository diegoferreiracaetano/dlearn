plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detekt) // Aplicado no root
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kover)
}

// Configuração Global do Detekt
detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

// Aplica os scripts modularizados
apply(from = "gradle/detekt.gradle.kts")
apply(from = "gradle/coverage.gradle.kts")

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
}
