package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.lb.astormemory.app.AppState
import io.lb.astormemory.settings.SettingsEvent
import io.lb.astormemory.settings.SettingsScreen
import io.lb.astormemory.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoute(
    navController: NavController,
    appState: AppState
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
            viewModel.onEvent(SettingsEvent.OnDarkModeChanged(it))
        },
        onChangeDynamicLayout = {
            viewModel.onEvent(SettingsEvent.OnDynamicLayoutChanged(it))
        },
        onChangeSoundEffectsEnabled = {
            viewModel.onEvent(SettingsEvent.OnSoundEffectsEnabledChanged(it))
        },
        onChangeCardsPerLine = {
            viewModel.onEvent(SettingsEvent.OnCardsPerLineChanged(it))
        },
        onChangeCardsPerColumn = {
            viewModel.onEvent(SettingsEvent.OnCardsPerColumnChanged(it))
        }
    )
}
