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
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.common.commonShared)
            implementation(projects.platform)
        }
    }
}

android {
    namespace = "io.lb.astormemory.game.ds"
}
