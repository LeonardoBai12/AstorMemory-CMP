plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinCocoapods) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ktor.plugin) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.ksp) apply true
    alias(libs.plugins.mockmp) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.detekt) apply true
}

detekt {
    source.setFrom(
        "composeApp/src",
        "game/domain/src",
        "game/data/src",
        "designsystem/src",
        "platform/src",
        "impl/database/src",
        "common/common_shared/src",
        "common/common_data/src"
    )
    buildUponDefaultConfig = true
    parallel = true
}
