import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import provider.libs

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.configure<DetektExtension> {
                config.setFrom(rootProject.files("detekt.yml"))
                buildUponDefaultConfig = true
                parallel = true
            }

            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-formatting").get())
            }
        }
    }
}
