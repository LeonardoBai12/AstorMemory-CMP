import extensions.debugImplementation
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import provider.libs

/**
 * Plugin to apply Compose Multiplatform conventions.
 */
class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    /**
     * Applies the Compose Multiplatform conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(libs.findLibrary("compose-runtime").get())
                        implementation(libs.findLibrary("compose-foundation-multiplatform").get())
                        implementation(libs.findLibrary("compose-material3-multiplatform").get())
                        implementation(libs.findLibrary("compose-ui-multiplatform").get())
                        implementation(libs.findLibrary("compose-components-resources").get())
                        implementation(libs.findLibrary("compose-components-uiToolingPreview").get())
                        implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                        implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("compose-preview-multiplatform").get())
                        implementation(libs.findLibrary("androidx-activity-compose").get())
                    }
                }
            }

            dependencies {
                with(libs) {
                    debugImplementation(findLibrary("compose-uiTooling-multiplatform").get())
                }
            }
        }
    }
}
