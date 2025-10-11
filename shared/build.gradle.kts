import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildconfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        val iosMain by creating {
            dependsOn(commonMain.get())
        }

        iosArm64Main.get().dependsOn(iosMain)
        iosSimulatorArm64Main.get().dependsOn(iosMain)

        commonMain.dependencies {
            api("io.insert-koin:koin-core:4.1.0")
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.kotlinx.serialization.json)
            
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.wasm)
        }
    }
}

android {
    namespace = "com.diegoferreiracaetano.dlearn.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

buildConfig {
    packageName("com.diegoferreiracaetano.dlearn.shared")
    buildConfigField("String", "THE_MOVIE_DB_BASE_URL", providers.gradleProperty("THE_MOVIE_DB_BASE_URL").get())
    buildConfigField("String", "THE_MOVIE_DB_API_KEY", providers.gradleProperty("THE_MOVIE_DB_API_KEY").get())
}
