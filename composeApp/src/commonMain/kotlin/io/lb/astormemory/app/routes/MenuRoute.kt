package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import io.lb.astormemory.app.AppState
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.menu.MenuEvent
import io.lb.astormemory.menu.MenuScreen
import io.lb.astormemory.menu.MenuViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MenuRoute(
    navController: NavController,
    appState: AppState,
    prefs: AppPreferences,
    onQuitApp: () -> Unit
) {
    val viewModel = koinViewModel<MenuViewModel>()

    LaunchedEffect(appState.isMuted) {
        viewModel.onEvent(MenuEvent.OnStart)
    }

    MenuScreen(
        navController = navController,
        isDarkMode = appState.isDarkMode,
        initialAmount = appState.initialAmount,
        isMuted = appState.isMuted,
        onChangeMuted = {
            appState.isMuted = it
            prefs.putBoolean(PreferencesKeys.IS_MUTED, it)
        },
        onButtonClicked = { viewModel.onEvent(MenuEvent.OnButtonClicked) },
        onClickQuit = onQuitApp,
        onChangeAmount = {
            appState.initialAmount = it
            prefs.putInt(PreferencesKeys.LAST_AMOUNT, it)
        }
    )
}
