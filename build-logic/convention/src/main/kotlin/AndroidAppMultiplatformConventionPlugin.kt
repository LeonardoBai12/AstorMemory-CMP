import com.android.build.api.dsl.ApplicationExtension
import extensions.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import provider.libs

/**
 * Plugin to apply Android Application conventions for multiplatform projects.
 */
class AndroidAppMultiplatformConventionPlugin : Plugin<Project> {
    /**
     * Applies the Android Application conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
                
                configureKotlinAndroid(this)
                
                defaultConfig {
                    targetSdk = libs.findVersion("android-targetSdk")
                        .get()
                        .toString()
                        .toInt()
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}