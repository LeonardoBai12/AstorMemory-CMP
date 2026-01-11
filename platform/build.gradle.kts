plugins {
    alias(libs.plugins.androidLibrary)
    id("io.lb.kotlin.multiplatform")
    id("io.lb.compose.multiplatform")
    id("io.lb.android.library.multiplatform")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.common.commonShared)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "io.lb.astormemory.game.platform"
}
