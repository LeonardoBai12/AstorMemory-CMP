package io.lb.astormemory.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.lb.astormemory.app.routes.GameOverRoute
import io.lb.astormemory.app.routes.GameRoute
import io.lb.astormemory.app.routes.HighScoresRoute
import io.lb.astormemory.app.routes.MenuRoute
import io.lb.astormemory.app.routes.SettingsRoute
import io.lb.astormemory.game.ds.components.ButtonSoundPlayer
import io.lb.astormemory.game.ds.components.LocalButtonSoundPlayer
import io.lb.astormemory.game.ds.effects.AstorMemoryTransitions
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(InternalResourceApi::class)
@Composable
fun AstorMemoryApp(onQuitApp: () -> Unit) {
    val navController = rememberNavController()
    val isSystemInDarkMode = isSystemInDarkTheme()
    val appViewModel = koinViewModel<AppViewModel> { parametersOf(isSystemInDarkMode) }
    val loadedAppState by appViewModel.state.collectAsState()
    val buttonSoundPlayer = remember(appViewModel) {
        object : ButtonSoundPlayer {
            override fun playClick() = appViewModel.onEvent(AppEvent.OnButtonClicked)
            override fun playOptionSelected() = appViewModel.onEvent(AppEvent.OnOptionSelected)
        }
    }

    // Nothing is drawn until the saved settings are loaded, so the theme doesn't flicker.
    val appState = loadedAppState ?: return

    CompositionLocalProvider(LocalButtonSoundPlayer provides buttonSoundPlayer) {
        AstorMemoryChallengeTheme(darkTheme = appState.isDarkMode) {
            NavHost(
                navController = navController,
                startDestination = AstorMemoryRoutes.Menu,
                enterTransition = { AstorMemoryTransitions.defaultEnterTransition },
                exitTransition = { AstorMemoryTransitions.defaultExitTransition },
                popEnterTransition = { AstorMemoryTransitions.defaultPopEnterTransition },
                popExitTransition = { AstorMemoryTransitions.defaultPopExitTransition }
            ) {
                composable<AstorMemoryRoutes.Menu> {
                    MenuRoute(navController, appState, onQuitApp)
                }

                composable<AstorMemoryRoutes.Game>(
                    enterTransition = { AstorMemoryTransitions.gameEnterTransition },
                    exitTransition = { AstorMemoryTransitions.gameExitTransition }
                ) { backStackEntry ->
                    GameRoute(backStackEntry, navController, appState)
                }

                composable<AstorMemoryRoutes.HighScores> {
                    HighScoresRoute(navController, appState.isDarkMode)
                }

                composable<AstorMemoryRoutes.GameOver>(
                    enterTransition = { AstorMemoryTransitions.gameOverEnterTransition }
                ) { backStackEntry ->
                    GameOverRoute(backStackEntry, navController, appState.isDarkMode)
                }

                composable<AstorMemoryRoutes.Settings> {
                    SettingsRoute(navController, appState)
                }
            }
        }
    }
}
