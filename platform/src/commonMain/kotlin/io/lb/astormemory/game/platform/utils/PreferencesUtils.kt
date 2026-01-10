package io.lb.astormemory.game.platform.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.lb.astormemory.game.platform.preferences.AppPreferences

object PreferencesKeys {
    const val IS_DARK_MODE = "isDarkMode"
    const val IS_MUTED = "isMuted"
    const val IS_DYNAMIC_LAYOUT = "isDynamicLayout"
    const val IS_SOUND_EFFECTS_ENABLED = "isSoundEffectsEnabled"
    const val CARDS_PER_LINE = "cardsPerLine"
    const val CARDS_PER_COLUMN = "cardsPerColumn"
    const val LAST_AMOUNT = "lastAmount"
}

@Composable
fun localDarkMode(prefs: AppPreferences): Boolean {
    val systemDarkMode = isSystemInDarkTheme()
    return prefs.getBoolean(PreferencesKeys.IS_DARK_MODE, systemDarkMode)
}
