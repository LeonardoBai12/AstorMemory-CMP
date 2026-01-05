plugins {
    id("io.lb.android.library.multiplatform")
    id("io.lb.kotlin.multiplatform")
}

android {
    namespace = "io.lb.astormemory.common.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.commonShared)
        }
    }
}