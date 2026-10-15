package io.lb.astormemory.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.lb.astormemory.app.routes.GameOverRoute
import io.lb.astormemory.app.routes.GameRoute
import io.lb.astormemory.app.routes.HighScoresRoute
import io.lb.astormemory.app.routes.MenuRoute
import io.lb.astormemory.app.routes.SettingsRoute
import io.lb.astormemory.game.ds.effects.AstorMemoryTransitions
import io.lb.astormemory.game.domain.repository.SFXAndMusicRepository
import io.lb.astormemory.game.platform.preferences.AppPreferences
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.compose.koinInject

@OptIn(InternalResourceApi::class)
@Composable
@Preview
fun AstorMemoryApp(onQuitApp: () -> Unit) {
    val navController = rememberNavController()
    val prefs: AppPreferences = koinInject()
    val sfxAndMusicRepository: SFXAndMusicRepository = koinInject()
    val appState = rememberAppState(prefs)

    DisposableEffect(Unit) {
        onDispose {
            sfxAndMusicRepository.release()
        }
    }

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
                MenuRoute(navController, appState, prefs, onQuitApp)
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
                SettingsRoute(navController, appState, prefs)
            }
        }
    }
}