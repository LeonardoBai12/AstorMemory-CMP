package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import io.lb.astormemory.gameover.GameOverEvent
import io.lb.astormemory.gameover.GameOverScreen
import io.lb.astormemory.gameover.GameOverViewModel
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun GameOverRoute(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    isDarkMode: Boolean
) {
    val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()
    val viewModel = koinViewModel<GameOverViewModel> {
        parametersOf(gameOver.score, gameOver.amount)
    }

    LaunchedEffect(gameOver.score, gameOver.amount) {
        viewModel.onEvent(GameOverEvent.OnStart)
    }

    GameOverScreen(
        navController = navController,
        isDarkMode = isDarkMode,
        score = gameOver.score,
        amount = gameOver.amount
    )
}