package io.lb.astormemory.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import io.lb.astormemory.game.platform.utils.MusicManager
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
    val musicManager: MusicManager = koinInject()
    val appState = rememberAppState(prefs)

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
            musicManager.release()
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
                LaunchedEffect(appState.isMuted) {
                    if (!appState.isMuted) {
                        musicManager.play(MusicManager.MusicTrack.TITLE_SCREEN)
                    } else {
                        musicManager.pauseAll()
                    }
                }

                MenuScreen(
                    navController = navController,
                    isDarkMode = appState.isDarkMode,
                    initialAmount = appState.initialAmount,
                    isMuted = appState.isMuted,
                    isSoundEffectsEnabled = appState.isSoundEffectsEnabled,
                    onChangeMuted = {
                        appState.isMuted = it
                        prefs.putBoolean(PreferencesKeys.IS_MUTED, it)
                        if (it) {
                            musicManager.pauseAll()
                        } else {
                            musicManager.play(MusicManager.MusicTrack.TITLE_SCREEN)
                        }
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
                val game = backStackEntry.toRoute<AstorMemoryRoutes.Game>()

                LaunchedEffect(appState.isMuted, game.amount) {
                    if (!appState.isMuted) {
                        val (track, volume) = when {
                            game.amount == 1 -> MusicManager.MusicTrack.VICTORY_ROAD to 1.0f
                            game.amount < 8 -> MusicManager.MusicTrack.WILD to 1.0f
                            game.amount >= 20 -> MusicManager.MusicTrack.FINAL_BATTLE to 0.85f
                            game.amount >= 12 -> MusicManager.MusicTrack.GYM_LEADER to 0.85f
                            else -> MusicManager.MusicTrack.TRAINER to 0.85f
                        }
                        musicManager.play(track, volume)
                    } else {
                        musicManager.pauseAll()
                    }
                }

                GameRoute(backStackEntry, navController, appState, audioPlayer)
            }

            composable<AstorMemoryRoutes.HighScores> {
                LaunchedEffect(appState.isMuted) {
                    if (!appState.isMuted) {
                        musicManager.play(MusicManager.MusicTrack.HIGHSCORES)
                    } else {
                        musicManager.pauseAll()
                    }
                }

                HighScoresRoute(navController, appState.isDarkMode)
            }

            composable<AstorMemoryRoutes.GameOver>(
                enterTransition = { AstorMemoryTransitions.gameOverEnterTransition }
            ) { backStackEntry ->
                val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()

                LaunchedEffect(appState.isMuted, gameOver.score, gameOver.amount) {
                    if (!appState.isMuted) {
                        val track = when {
                            gameOver.score == 0 -> MusicManager.MusicTrack.LAVENDER
                            gameOver.amount >= 20 -> MusicManager.MusicTrack.VICTORY_FINAL
                            else -> MusicManager.MusicTrack.GAMEOVER
                        }
                        val volume = if (gameOver.score == 0) 0.95f else 1.0f
                        musicManager.play(track, volume)
                    } else {
                        musicManager.pauseAll()
                    }
                }

                GameOverRoute(backStackEntry, navController, appState.isDarkMode)
            }

            composable<AstorMemoryRoutes.Settings> {
                SettingsRoute(navController, appState, prefs)
            }
        }
    }
}