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

    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)

    // Auth
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    // Koin
    implementation(libs.koin.core)
    implementation("io.insert-koin:koin-ktor:4.1.0")

    // Swagger & OpenAPI
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.swagger)

    // Caching
    implementation(libs.ktor.server.caching.headers)

    // Status Pages
    implementation(libs.ktor.server.status.pages)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}

buildConfig {
    packageName("com.diegoferreiracaetano.dlearn.server")
    buildConfigField("String", "THE_MOVIE_DB_BASE_URL", providers.gradleProperty("THE_MOVIE_DB_BASE_URL").get())
    buildConfigField("String", "THE_MOVIE_DB_API_KEY", providers.gradleProperty("THE_MOVIE_DB_API_KEY").get())
}