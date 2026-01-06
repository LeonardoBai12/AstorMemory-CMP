import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "io.lb.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("KotlinMultiplatformConventionPlugin") {
            id = "io.lb.kotlin.multiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("ComposeMultiplatformConventionPlugin") {
            id = "io.lb.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("AndroidAppMultiplatformConventionPlugin") {
            id = "io.lb.android.app.multiplatform"
            implementationClass = "AndroidAppMultiplatformConventionPlugin"
        }
        register("AndroidLibraryMultiplatformConventionPlugin") {
            id = "io.lb.android.library.multiplatform"
            implementationClass = "AndroidLibraryMultiplatformConventionPlugin"
        }
        register("AstorResourceGeneratorPlugin") {
            id = "io.lb.astor.resource.generator"
            implementationClass = "AstorResourceGeneratorPlugin"
        }
    }
}