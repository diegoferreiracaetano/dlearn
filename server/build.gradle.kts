plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildconfig)
    application
}

group = "com.diegoferreiracaetano.dlearn"
version = "1.0.0"
application {
    mainClass.set("com.diegoferreiracaetano.dlearn.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)

    // Added dependencies
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)


    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}

buildConfig {
    packageName("com.diegoferreiracaetano.dlearn.server")
    buildConfigField("String", "THE_MOVIE_DB_BASE_URL", providers.gradleProperty("THE_MOVIE_DB_BASE_URL").get())
    buildConfigField("String", "THE_MOVIE_DB_API_KEY", providers.gradleProperty("THE_MOVIE_DB_API_KEY").get())
}