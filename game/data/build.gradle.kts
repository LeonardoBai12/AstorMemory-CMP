plugins {
    alias(libs.plugins.androidLibrary)
    id("io.lb.kotlin.multiplatform")
    id("io.lb.compose.multiplatform")
    id("io.lb.android.library.multiplatform")
    id("io.lb.astor.resource.generator")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.game.domain)
            implementation(projects.common.commonData)
            implementation(projects.common.commonShared)
        }
    }
}

android {
    namespace = "io.lb.astormemory.game.data"
}
