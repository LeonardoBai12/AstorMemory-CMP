package io.lb.astormemory

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.lb.astormemory.game.GameScreen
import io.lb.astormemory.game.GameViewModel
import io.lb.astormemory.menu.MenuScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.gameover.GameOverScreen
import io.lb.astormemory.highscore.HighScoresScreen
import io.lb.astormemory.highscore.ScoreViewModel
import io.lb.astormemory.navigation.AstorMemoryRoutes
import io.lb.astormemory.settings.SettingsScreen
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(InternalResourceApi::class)
@Composable
@Preview
fun AstorMemoryApp() {
    val navController = rememberNavController()

    AstorMemoryChallengeTheme(darkTheme = true) {
        NavHost(
            navController = navController,
            startDestination = AstorMemoryRoutes.Menu,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            composable<AstorMemoryRoutes.Menu> {
                MenuScreen(
                    navController = navController,
                    isDarkMode = true,
                    initialAmount = 9,
                    isMuted = false,
                    onChangeMuted = {},
                    onClickQuit = {},
                    onChangeAmount = {}
                )
            }

            composable<AstorMemoryRoutes.Game> { backStackEntry ->
                val game = backStackEntry.toRoute<AstorMemoryRoutes.Game>()
                val viewModel = koinViewModel<GameViewModel> { parametersOf(game.amount) }
                val state by viewModel.state.collectAsState()
                GameScreen(
                    navController = navController,
                    state = state,
                    eventFlow = viewModel.eventFlow,
                    onEvent = viewModel::onEvent,
                    isDarkMode = true,
                    cardsPerLine = 3,
                    cardsPerColumn = 7,
                    onCardFlipped = {
                        // Sound effect can be played here
                    },
                    onCardMatched = {
                        // Sound effect can be played here
                    }
                )
            }

            composable<AstorMemoryRoutes.HighScores> {
                val viewModel = koinViewModel<ScoreViewModel>()
                val state by viewModel.state.collectAsState()

                HighScoresScreen(
                    navController = navController,
                    state = state,
                    onEvent = viewModel::onEvent,
                    isDarkMode = true
                )
            }

            composable<AstorMemoryRoutes.GameOver> { backStackEntry ->
                val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()
                GameOverScreen(
                    navController = navController,
                    isDarkMode = true,
                    score = gameOver.score,
                    amount = gameOver.amount
                )
            }

            composable<AstorMemoryRoutes.Settings> {
                SettingsScreen(
                    navController = navController,
                    cardsPerLine = 3,
                    cardsPerColumn = 7,
                    isDarkMode = true,
                    onChangeDarkMode = { },
                    onChangeCardsPerLine = { },
                    onChangeCardsPerColumn = { }
                )

            }
        }
    }
}
