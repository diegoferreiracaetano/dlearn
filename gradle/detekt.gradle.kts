// gradle/detekt.gradle.kts
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Como o plugin já está no root build.gradle.kts, o import deve funcionar agora se o classloader for compartilhado.
// Para scripts .kts aplicados via apply(from), podemos precisar de novo do buildscript no topo para garantir o classpath.

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
    }
}

subprojects {
    val subProject = this
    
    // Aplica o plugin a todos os subprojetos para que eles tenham suas próprias tasks 'detekt'
    subProject.apply(plugin = "io.gitlab.arturbosch.detekt")

    // Configuração básica por subprojeto (usando afterEvaluate para garantir que a extensão exista)
    subProject.afterEvaluate {
        if (subProject.extensions.findByName("detekt") != null) {
            subProject.tasks.withType(Detekt::class.java).configureEach {
                jvmTarget = "17"
                exclude("**/build/**")
                exclude("**/resources/**")
                exclude("**/generated/**")

                reports {
                    html.required.set(true)
                    xml.required.set(true)
                }
            }
        }
    }
}

// Tasks agregadoras na raiz (registradas no projeto onde o script é aplicado - a raiz)
tasks.register("detektAll") {
    group = "verification"
    description = "Executa a análise do Detekt em todos os subprojetos."
    // Depende de todas as tasks do tipo Detekt em todos os subprojetos
    dependsOn(subprojects.map { it.tasks.withType(Detekt::class.java) })
}

tasks.register("detektBaselineAll") {
    group = "verification"
    description = "Gera o baseline do Detekt para todos os subprojetos."
    dependsOn(subprojects.map { it.tasks.withType(DetektCreateBaselineTask::class.java) })
}
