plugins {
    id("io.lb.android.app.multiplatform")
    id("io.lb.kotlin.multiplatform")
    id("io.lb.compose.multiplatform")
}

android {
    namespace = "io.lb.astormemory.app"

    defaultConfig {
        applicationId = "io.lb.astormemory.app"
        versionCode = 11
        versionName = "2.1.0"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.navigation.compose)
            implementation(compose.materialIconsExtended)
            implementation(projects.designsystem)
            implementation(projects.platform)
            implementation(projects.common.commonShared)
            implementation(projects.impl.database)
            implementation(projects.game.data)
            implementation(projects.game.domain)
        }
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.integrity)
            implementation(libs.koin.android)
        }
    }
}