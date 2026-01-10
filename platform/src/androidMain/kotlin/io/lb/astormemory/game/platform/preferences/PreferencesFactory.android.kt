package io.lb.astormemory.game.platform.preferences

import android.content.Context

actual class PreferencesFactory(private val context: Context) {
    actual fun create(): AppPreferences {
        return AndroidAppPreferences(context)
    }
}
