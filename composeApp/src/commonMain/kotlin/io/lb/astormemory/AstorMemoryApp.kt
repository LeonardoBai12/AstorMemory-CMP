package io.lb.astormemory

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
import io.lb.astormemory.navigation.AstorMemoryRoutes
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
            startDestination = AstorMemoryRoutes.Menu
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "High Scores Screen",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            composable<AstorMemoryRoutes.GameOver> { backStackEntry ->
                val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Game Over Screen - Score: ${gameOver.score}, Amount: ${gameOver.amount}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            composable<AstorMemoryRoutes.Settings> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Settings Screen",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}