package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import io.lb.astormemory.app.AppState
import io.lb.astormemory.app.getEffectiveCardsPerColumn
import io.lb.astormemory.app.getEffectiveCardsPerLine
import io.lb.astormemory.game.GameEvent
import io.lb.astormemory.game.GameScreen
import io.lb.astormemory.game.GameViewModel
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameRoute(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    appState: AppState
) {
    val game = backStackEntry.toRoute<AstorMemoryRoutes.Game>()
    val viewModel = koinViewModel<GameViewModel> { parametersOf(game.amount) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(game.amount) {
        viewModel.onEvent(GameEvent.GameStarted)
    }

    GameScreen(
        navController = navController,
        state = state,
        eventFlow = viewModel.eventFlow,
        onEvent = viewModel::onEvent,
        isDarkMode = appState.isDarkMode,
        cardsPerLine = getEffectiveCardsPerLine(appState, game.amount),
        cardsPerColumn = getEffectiveCardsPerColumn(appState, game.amount)
    )
}
