package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.lb.astormemory.app.AppState
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.settings.SettingsEvent
import io.lb.astormemory.settings.SettingsScreen
import io.lb.astormemory.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoute(
    navController: NavController,
    appState: AppState,
    prefs: AppPreferences
) {
    val viewModel = koinViewModel<SettingsViewModel>()

    SettingsScreen(
        navController = navController,
        cardsPerLine = appState.cardsPerLine,
        cardsPerColumn = appState.cardsPerColumn,
        isDarkMode = appState.isDarkMode,
        isDynamicLayout = appState.isDynamicLayout,
        isSoundEffectsEnabled = appState.isSoundEffectsEnabled,
        onChangeDarkMode = {
            viewModel.onEvent(SettingsEvent.OnOptionChanged)
            appState.isDarkMode = it
            prefs.putBoolean(PreferencesKeys.IS_DARK_MODE, it)
        },
        onChangeDynamicLayout = {
            viewModel.onEvent(SettingsEvent.OnOptionChanged)
            appState.isDynamicLayout = it
            prefs.putBoolean(PreferencesKeys.IS_DYNAMIC_LAYOUT, it)
        },
        onChangeSoundEffectsEnabled = {
            appState.isSoundEffectsEnabled = it
            prefs.putBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, it)
            viewModel.onEvent(SettingsEvent.OnOptionChanged)
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
