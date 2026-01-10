package io.lb.astormemory

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import astormemory.composeapp.generated.resources.Res
import io.lb.astormemory.dynamic.calculateCardsPerColumn
import io.lb.astormemory.dynamic.calculateCardsPerLine
import io.lb.astormemory.game.GameScreen
import io.lb.astormemory.game.GameViewModel
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.menu.MenuScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.gameover.GameOverScreen
import io.lb.astormemory.highscore.HighScoresScreen
import io.lb.astormemory.highscore.ScoreViewModel
import io.lb.astormemory.navigation.AstorMemoryRoutes
import io.lb.astormemory.settings.SettingsScreen
import org.jetbrains.compose.resources.InternalResourceApi
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(InternalResourceApi::class)
@Composable
@Preview
fun AstorMemoryApp(
    onQuitApp: () -> Unit
) {
    val navController = rememberNavController()
    val prefs: AppPreferences = koinInject()
    val audioPlayer: AudioPlayer = koinInject()
    val localIsDarkMode = localDarkMode(prefs)
    var isDarkMode by remember { mutableStateOf(localIsDarkMode) }
    var isMuted by remember { mutableStateOf(prefs.getBoolean("isMuted", false)) }
    var isDynamicLayout by remember { mutableStateOf(prefs.getBoolean("isDynamicLayout", true)) }
    var isSoundEffectsEnabled by remember { mutableStateOf(prefs.getBoolean("isSoundEffectsEnabled", true)) }
    var cardsPerLine by remember { mutableIntStateOf(prefs.getInt("cardsPerLine", 4)) }
    var cardsPerColumn by remember { mutableIntStateOf(prefs.getInt("cardsPerColumn", 6)) }
    var initialAmount by remember { mutableIntStateOf(prefs.getInt("lastAmount", 9)) }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    AstorMemoryChallengeTheme(darkTheme = isDarkMode) {
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
                    isDarkMode = isDarkMode,
                    initialAmount = initialAmount,
                    isMuted = isMuted,
                    onChangeMuted = {
                        isMuted = it
                        prefs.putBoolean("isMuted", it)
                    },
                    onClickQuit = onQuitApp,
                    onChangeAmount = {
                        initialAmount = it
                        prefs.putInt("lastAmount", it)
                    }
                )
            }

            composable<AstorMemoryRoutes.Game>(
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    scaleOut(
                        targetScale = 0.9f,
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) { backStackEntry ->
                val game = backStackEntry.toRoute<AstorMemoryRoutes.Game>()
                val viewModel = koinViewModel<GameViewModel> { parametersOf(game.amount) }
                val state by viewModel.state.collectAsState()
                GameScreen(
                    navController = navController,
                    state = state,
                    eventFlow = viewModel.eventFlow,
                    onEvent = viewModel::onEvent,
                    isDarkMode = isDarkMode,
                    cardsPerLine = if (isDynamicLayout) calculateCardsPerLine(game.amount) else cardsPerLine,
                    cardsPerColumn = if (isDynamicLayout) calculateCardsPerColumn(game.amount) else cardsPerColumn,
                    onCardFlipped = {
                        if (isSoundEffectsEnabled) {
                            playFlipSoundEffect(isMuted, audioPlayer)
                        }
                    },
                    onCardMatched = { matches ->
                        if (isSoundEffectsEnabled) {
                            playMatchSound(matches, game, isMuted, audioPlayer)
                        }
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
                    isDarkMode = isDarkMode
                )
            }

            composable<AstorMemoryRoutes.GameOver>(
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn(animationSpec = tween(500))
                }
            ) { backStackEntry ->
                val gameOver = backStackEntry.toRoute<AstorMemoryRoutes.GameOver>()
                GameOverScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    score = gameOver.score,
                    amount = gameOver.amount
                )
            }

            composable<AstorMemoryRoutes.Settings> {
                SettingsScreen(
                    navController = navController,
                    cardsPerLine = cardsPerLine,
                    cardsPerColumn = cardsPerColumn,
                    isDarkMode = isDarkMode,
                    isDynamicLayout = isDynamicLayout,
                    isSoundEffectsEnabled = isSoundEffectsEnabled,
                    onChangeDarkMode = {
                        isDarkMode = it
                        prefs.putBoolean("isDarkMode", it)
                    },
                    onChangeDynamicLayout = {
                        isDynamicLayout = it
                        prefs.putBoolean("isDynamicLayout", it)
                    },
                    onChangeSoundEffectsEnabled = {
                        isSoundEffectsEnabled = it
                        prefs.putBoolean("isSoundEffectsEnabled", it)
                    },
                    onChangeCardsPerLine = {
                        cardsPerLine = it
                        prefs.putInt("cardsPerLine", it)
                    },
                    onChangeCardsPerColumn = {
                        cardsPerColumn = it
                        prefs.putInt("cardsPerColumn", it)
                    }
                )
            }
        }
    }
}

private fun playFlipSoundEffect(
    isMuted: Boolean,
    audioPlayer: AudioPlayer
) {
    val volume = if (isMuted) FLIP_VOLUME / 2 else FLIP_VOLUME
    audioPlayer.playSound("flip_card_final", volume)
}

private fun playMatchSound(
    matches: Int,
    game: AstorMemoryRoutes.Game,
    isMuted: Boolean,
    audioPlayer: AudioPlayer
) {
    val volume = if (matches > game.amount * 3 / 4) {
        if (isMuted) MATCH_VOLUME / PLAY_MATCH_DIVIDER else MATCH_VOLUME
    } else {
        if (isMuted) MATCH_VOLUME / 2 else MATCH_VOLUME
    }
    val soundFile = if (matches > game.amount * 3 / 4) {
        "match_effect"
    } else {
        "match_card_init"
    }
    audioPlayer.playSound(soundFile, volume)
}

@Composable
private fun localDarkMode(
    sharedPref: AppPreferences,
    isDarkMode: Boolean = isSystemInDarkTheme()
): Boolean = sharedPref.getBoolean(
    "isDarkMode",
    isDarkMode
)

private const val MATCH_VOLUME = 0.7f
private const val FLIP_VOLUME = 0.95f
private const val PLAY_MATCH_DIVIDER = 2f
