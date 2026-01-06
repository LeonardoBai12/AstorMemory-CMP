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
            implementation(projects.common.commonShared)
            implementation(projects.game.data)
        }
    }
}