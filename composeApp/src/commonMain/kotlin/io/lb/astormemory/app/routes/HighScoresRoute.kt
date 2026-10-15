package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import io.lb.astormemory.highscore.HighScoresScreen
import io.lb.astormemory.highscore.ScoreViewModel
import io.lb.astormemory.highscore.ScoresEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HighScoresRoute(navController: NavController, isDarkMode: Boolean) {
    val viewModel = koinViewModel<ScoreViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ScoresEvent.OnPlaySong)
    }

    HighScoresScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        isDarkMode = isDarkMode
    )
}
