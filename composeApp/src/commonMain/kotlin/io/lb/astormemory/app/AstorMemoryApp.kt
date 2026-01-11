package io.lb.astormemory.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.lb.astormemory.app.routes.GameOverRoute
import io.lb.astormemory.app.routes.GameRoute
import io.lb.astormemory.app.routes.HighScoresRoute
import io.lb.astormemory.app.routes.SettingsRoute
import io.lb.astormemory.game.ds.effects.AstorMemoryTransitions
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.menu.MenuScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.compose.koinInject

@OptIn(InternalResourceApi::class)
@Composable
@Preview
fun AstorMemoryApp(onQuitApp: () -> Unit) {
    val navController = rememberNavController()
    val prefs: AppPreferences = koinInject()
    val audioPlayer: AudioPlayer = koinInject()
    val appState = rememberAppState(prefs)

    DisposableEffect(Unit) {
        onDispose { audioPlayer.release() }
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
                MenuScreen(
                    navController = navController,
                    isDarkMode = appState.isDarkMode,
                    initialAmount = appState.initialAmount,
                    isMuted = appState.isMuted,
                    isSoundEffectsEnabled = appState.isSoundEffectsEnabled,
                    onChangeMuted = {
                        appState.isMuted = it
                        prefs.putBoolean(PreferencesKeys.IS_MUTED, it)
                    },
                    onClickQuit = onQuitApp,
                    onChangeAmount = {
                        appState.initialAmount = it
                        prefs.putInt(PreferencesKeys.LAST_AMOUNT, it)
                    }
                )
            }

            composable<AstorMemoryRoutes.Game>(
                enterTransition = { AstorMemoryTransitions.gameEnterTransition },
                exitTransition = { AstorMemoryTransitions.gameExitTransition }
            ) { backStackEntry ->
                GameRoute(backStackEntry, navController, appState, audioPlayer)
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

