// gradle/coverage.gradle.kts
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

val catalogs = extensions.getByType(VersionCatalogsExtension::class.java)
val libsCatalog = catalogs.named("libs")
val jacocoVersionStr = libsCatalog.findVersion("jacoco").get().requiredVersion

subprojects {
    val subproject = this
    pluginManager.withPlugin("com.android.application") {
        registerJacocoAndroidTask(subproject)
    }
    pluginManager.withPlugin("com.android.library") {
        registerJacocoAndroidTask(subproject)
    }
}

fun registerJacocoAndroidTask(project: Project) {
    project.pluginManager.apply("jacoco")
    
    project.extensions.configure(JacocoPluginExtension::class.java) {
        toolVersion = jacocoVersionStr
    }

    project.tasks.register<JacocoReport>("jacocoAndroidTestReport") {
        group = "Reporting"
        dependsOn(project.tasks.matching { it.name == "testDebugUnitTest" || it.name == "connectedDebugAndroidTest" })

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        val buildDir = project.layout.buildDirectory.get().asFile
        val excludes = listOf("**/R.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")

        classDirectories.setFrom(project.fileTree(buildDir.resolve("tmp/kotlin-classes/debug")) { exclude(excludes) })
        sourceDirectories.setFrom(project.files("${project.projectDir}/src/main/java", "${project.projectDir}/src/commonMain/kotlin"))
        executionData.setFrom(project.fileTree(buildDir) {
            include("outputs/unit_test_code_coverage/debugUnitTest/*.exec", "outputs/code_coverage/debugAndroidTest/connected/**/*.ec")
        })
    }
}
