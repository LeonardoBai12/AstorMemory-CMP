package io.lb.astormemory.game.platform.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferencesKeys {
    val IS_DARK_MODE = booleanPreferencesKey("isDarkMode")
    val IS_MUTED = booleanPreferencesKey("isMuted")
    val IS_DYNAMIC_LAYOUT = booleanPreferencesKey("isDynamicLayout")
    val IS_SOUND_EFFECTS_ENABLED = booleanPreferencesKey("isSoundEffectsEnabled")
    val CARDS_PER_LINE = intPreferencesKey("cardsPerLine")
    val CARDS_PER_COLUMN = intPreferencesKey("cardsPerColumn")
    val LAST_AMOUNT = intPreferencesKey("lastAmount")

    val BOOLEAN_KEYS = listOf(IS_DARK_MODE, IS_MUTED, IS_DYNAMIC_LAYOUT, IS_SOUND_EFFECTS_ENABLED)
    val INT_KEYS = listOf(CARDS_PER_LINE, CARDS_PER_COLUMN, LAST_AMOUNT)
}
