import com.android.build.api.dsl.LibraryExtension
import extensions.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import provider.libs

/**
 * Plugin to apply Android Library conventions for multiplatform projects.
 */
class AndroidLibraryMultiplatformConventionPlugin : Plugin<Project> {
    /**
     * Applies the Android Library conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}