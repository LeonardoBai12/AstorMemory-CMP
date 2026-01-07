plugins {
    id("io.lb.android.app.multiplatform")
    id("io.lb.kotlin.multiplatform")
    id("io.lb.compose.multiplatform")
}

android {
    namespace = "io.lb.astormemory"

    defaultConfig {
        applicationId = "io.lb.astormemory"
        versionCode = 1
        versionName = "1.0"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(projects.common.commonShared)
            implementation(projects.game.data)
            implementation(projects.impl.database)
            implementation(projects.game.data)
            implementation(projects.game.domain)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}