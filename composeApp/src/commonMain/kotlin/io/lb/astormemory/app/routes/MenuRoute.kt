package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import io.lb.astormemory.app.AppState
import io.lb.astormemory.menu.MenuEvent
import io.lb.astormemory.menu.MenuScreen
import io.lb.astormemory.menu.MenuViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MenuRoute(
    navController: NavController,
    appState: AppState,
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
        onChangeMuted = { viewModel.onEvent(MenuEvent.OnMutedChanged(it)) },
        onButtonClicked = { viewModel.onEvent(MenuEvent.OnButtonClicked) },
        onClickQuit = onQuitApp,
        onChangeAmount = { viewModel.onEvent(MenuEvent.OnAmountChanged(it)) }
    )
}
