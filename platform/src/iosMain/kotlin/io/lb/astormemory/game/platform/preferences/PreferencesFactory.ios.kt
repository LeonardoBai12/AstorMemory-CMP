package io.lb.astormemory.game.platform.preferences

actual class PreferencesFactory {
    actual fun create(): AppPreferences {
        return IosAppPreferences()
    }
}
