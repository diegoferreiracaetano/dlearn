import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildconfig)
}

buildConfig {
    packageName("com.diegoferreiracaetano.dlearn")
    buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", providers.gradleProperty("GOOGLE_WEB_CLIENT_ID").getOrElse(""))
    buildConfigField("String", "APPLE_CLIENT_ID", providers.gradleProperty("APPLE_CLIENT_ID").getOrElse(""))
    buildConfigField("String", "FACEBOOK_APP_ID", providers.gradleProperty("FACEBOOK_APP_ID").getOrElse(""))
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

        commonMain.dependencies {
            api(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            api(libs.androidx.lifecycle.viewmodel)
            api(libs.androidx.lifecycle.runtimeCompose)
            
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            api(libs.multiplatform.settings)
            api(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.no.arg)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.common)
            
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.googleid)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.firebase.auth)
            implementation(libs.firebase.common)
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
