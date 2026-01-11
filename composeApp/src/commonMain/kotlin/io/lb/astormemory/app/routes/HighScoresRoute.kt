package io.lb.astormemory.app.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import io.lb.astormemory.highscore.HighScoresScreen
import io.lb.astormemory.highscore.ScoreViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HighScoresRoute(navController: NavController, isDarkMode: Boolean) {
    val viewModel = koinViewModel<ScoreViewModel>()
    val state by viewModel.state.collectAsState()
    
    HighScoresScreen(
        navController = navController,
        state = state,
        onEvent = viewModel::onEvent,
        isDarkMode = isDarkMode
    )
}
