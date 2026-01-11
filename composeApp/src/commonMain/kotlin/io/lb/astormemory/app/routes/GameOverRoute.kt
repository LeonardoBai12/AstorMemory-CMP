package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import io.lb.astormemory.gameover.GameOverScreen
import io.lb.astormemory.navigation.AstorMemoryRoutes

@Composable
fun GameOverRoute(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    isDarkMode: Boolean
) {
    val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()
    GameOverScreen(
        navController = navController,
        isDarkMode = isDarkMode,
        score = gameOver.score,
        amount = gameOver.amount
    )
}