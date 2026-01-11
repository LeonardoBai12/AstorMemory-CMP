package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.lb.astormemory.app.AppState
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.AstorMemoryAudio
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun SettingsRoute(
    navController: NavController,
    appState: AppState,
    prefs: AppPreferences
) {
    val audioPlayer: AudioPlayer = koinInject()

    SettingsScreen(
        navController = navController,
        cardsPerLine = appState.cardsPerLine,
        cardsPerColumn = appState.cardsPerColumn,
        isDarkMode = appState.isDarkMode,
        isDynamicLayout = appState.isDynamicLayout,
        isSoundEffectsEnabled = appState.isSoundEffectsEnabled,
        onChangeDarkMode = {
            if (appState.isSoundEffectsEnabled) {
                AstorMemoryAudio.playFlipEffect(appState.isMuted, audioPlayer)
            }
            appState.isDarkMode = it
            prefs.putBoolean(PreferencesKeys.IS_DARK_MODE, it)
        },
        onChangeDynamicLayout = {
            if (appState.isSoundEffectsEnabled) {
                AstorMemoryAudio.playFlipEffect(appState.isMuted, audioPlayer)
            }
            appState.isDynamicLayout = it
            prefs.putBoolean(PreferencesKeys.IS_DYNAMIC_LAYOUT, it)
        },
        onChangeSoundEffectsEnabled = {
            appState.isSoundEffectsEnabled = it
            if (appState.isSoundEffectsEnabled) {
                AstorMemoryAudio.playFlipEffect(appState.isMuted, audioPlayer)
            }
            prefs.putBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, it)
        },
        onChangeCardsPerLine = {
            appState.cardsPerLine = it
            prefs.putInt(PreferencesKeys.CARDS_PER_LINE, it)
        },
        onChangeCardsPerColumn = {
            appState.cardsPerColumn = it
            prefs.putInt(PreferencesKeys.CARDS_PER_COLUMN, it)
        }
    )
}
