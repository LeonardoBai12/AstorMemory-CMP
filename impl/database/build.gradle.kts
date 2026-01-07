plugins {
    alias(libs.plugins.androidLibrary)
    id("io.lb.kotlin.multiplatform")
    id("io.lb.android.library.multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(projects.common.commonData)
            implementation(projects.common.commonShared)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

android {
    namespace = "io.lb.astormemory.database"
}

sqldelight {
    databases {
        create("AstorMemoryDatabase") {
            packageName.set("io.lb.astormemory.database")
        }
    }
}
