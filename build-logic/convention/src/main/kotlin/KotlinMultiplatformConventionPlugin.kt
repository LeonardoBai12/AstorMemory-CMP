import com.android.build.api.variant.impl.capitalizeFirstChar
import extensions.COMPILE_VERSION
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import provider.libs

/**
 * Plugin to apply Kotlin Multiplatform conventions.
 */
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    /**
     * Applies the Kotlin Multiplatform conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.fromTarget(COMPILE_VERSION.toString()))
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = project.name.capitalizeFirstChar()
                        isStatic = false
                    }
                    iosTarget.binaries.all {
                        linkerOpts.add("-lsqlite3")
                    }
                }

                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())
                        implementation(libs.findLibrary("kotlin-datetime").get())
                    }

                    commonTest.dependencies {
                        implementation(kotlin("test"))
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-android").get())
                    }
                }
            }

            configureKotlinMultiplatform()

            dependencies {
                with(libs) {
                    testImplementation(findLibrary("turbine").get())
                    testImplementation(findLibrary("assertk").get())
                    testImplementation(findLibrary("kotlinx-coroutines-test").get())
                    testImplementation(findLibrary("kotlin-test-common").get())
                    testImplementation(findLibrary("kotlin-test-annotations-common").get())
                }
            }
        }
    }

    private fun Project.configureKotlinMultiplatform() {
        tasks.withType(KotlinCompile::class.java).configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(COMPILE_VERSION.toString()))
                val warningsAsErrors: String? by project
                allWarningsAsErrors.set(warningsAsErrors.toBoolean())
                freeCompilerArgs.addAll(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
            }
        }
    }
}