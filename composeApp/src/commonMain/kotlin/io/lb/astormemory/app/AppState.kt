package io.lb.astormemory.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.lb.astormemory.dynamic.calculateCardsPerColumn
import io.lb.astormemory.dynamic.calculateCardsPerLine
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.game.platform.utils.localDarkMode

@Stable
class AppState(
    isDarkMode: Boolean,
    isMuted: Boolean,
    isDynamicLayout: Boolean,
    isSoundEffectsEnabled: Boolean,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    initialAmount: Int
) {
    var isDarkMode by mutableStateOf(isDarkMode)
    var isMuted by mutableStateOf(isMuted)
    var isDynamicLayout by mutableStateOf(isDynamicLayout)
    var isSoundEffectsEnabled by mutableStateOf(isSoundEffectsEnabled)
    var cardsPerLine by mutableIntStateOf(cardsPerLine)
    var cardsPerColumn by mutableIntStateOf(cardsPerColumn)
    var initialAmount by mutableIntStateOf(initialAmount)
}

@Composable
fun rememberAppState(prefs: AppPreferences): AppState {
    val localIsDarkMode = localDarkMode(prefs)
    return remember(prefs) {
        AppState(
            isDarkMode = localIsDarkMode,
            isMuted = prefs.getBoolean(PreferencesKeys.IS_MUTED, false),
            isDynamicLayout = prefs.getBoolean(PreferencesKeys.IS_DYNAMIC_LAYOUT, true),
            isSoundEffectsEnabled = prefs.getBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, true),
            cardsPerLine = prefs.getInt(PreferencesKeys.CARDS_PER_LINE, 4),
            cardsPerColumn = prefs.getInt(PreferencesKeys.CARDS_PER_COLUMN, 6),
            initialAmount = prefs.getInt(PreferencesKeys.LAST_AMOUNT, 9)
        )
    }
}

fun getEffectiveCardsPerLine(appState: AppState, amount: Int): Int {
    return if (appState.isDynamicLayout) calculateCardsPerLine(amount) else appState.cardsPerLine
}

fun getEffectiveCardsPerColumn(appState: AppState, amount: Int): Int {
    return if (appState.isDynamicLayout) calculateCardsPerColumn(amount) else appState.cardsPerColumn
}
