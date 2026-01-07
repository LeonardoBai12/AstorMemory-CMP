package io.lb.astormemory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.lb.astormemory.game.MenuScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import io.lb.astormemory.game.data.resources.AstorResourceProvider
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.navigation.MemoryGameScreens
import io.lb.astormemory.shared.model.AstorCard
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

@OptIn(InternalResourceApi::class)
@Composable
@Preview
fun AstorMemoryApp() {
    val navController = rememberNavController()

    AstorMemoryChallengeTheme(darkTheme = true) {
        NavHost(
            navController = navController,
            startDestination = MemoryGameScreens.Menu.name
        ) {
            composable(MemoryGameScreens.Menu.name) {
                MenuScreen(
                    navController = navController,
                    isDarkMode = true,
                    initialAmount = 5,
                    isMuted = false,
                    onChangeMuted = {},
                    onClickQuit = {},
                    onChangeAmount = {}
                )
            }

            composable("${MemoryGameScreens.Game.name}/{amount}") { backStackEntry ->
                val amount = backStackEntry.arguments?.getString("amount")?.toIntOrNull() ?: 5
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Game Screen - Amount: $amount",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            composable(MemoryGameScreens.HighScores.name) {
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

            composable("${MemoryGameScreens.GameOver.name}/{score}/{amount}") { backStackEntry ->
                val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
                val amount = backStackEntry.arguments?.getString("amount")?.toIntOrNull() ?: 5
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Game Over Screen - Score: $score, Amount: $amount",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            composable(MemoryGameScreens.Settings.name) {
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