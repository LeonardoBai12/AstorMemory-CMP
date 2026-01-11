package io.lb.astormemory.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import astormemory.composeapp.generated.resources.Res
import astormemory.composeapp.generated.resources.back
import astormemory.composeapp.generated.resources.missingno
import astormemory.composeapp.generated.resources.play_again
import astormemory.composeapp.generated.resources.score
import astormemory.composeapp.generated.resources.score_result
import astormemory.composeapp.generated.resources.with_card_pairs
import astormemory.composeapp.generated.resources.you_won
import io.lb.astormemory.game.ds.components.MemoryGameLogo
import io.lb.astormemory.game.ds.components.MemoryGameRedButton
import io.lb.astormemory.game.ds.components.MemoryGameWhiteButton
import io.lb.astormemory.game.ds.components.Narcisus
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.navigation.AstorMemoryRoutes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameOverScreen(
    navController: NavController,
    isDarkMode: Boolean,
    score: Int,
    amount: Int
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(Dimens.padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimens.bigPadding))

            MemoryGameLogo(
                Modifier
                    .fillMaxWidth(0.6f)
                    .heightIn(min = 60.dp, max = 120.dp)
            )

            Spacer(modifier = Modifier.height(Dimens.bigPadding))

            Text(
                text = if (amount == 0) {
                    "${stringResource(Res.string.you_won)}?"
                } else {
                    "${stringResource(Res.string.you_won)}!"
                },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.bigPadding))
            GameOverContent(score, amount)
            Spacer(modifier = Modifier.weight(1f))
            GameOverButtons(navController, isDarkMode, amount)
            Spacer(modifier = Modifier.height(Dimens.largePadding))
        }
    }
}

@Composable
private fun GameOverContent(score: Int, amount: Int) {
    if (score == 0) {
        Text(
            text = stringResource(Res.string.score),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.largePadding))
        Image(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .sizeIn(
                    minWidth = 120.dp,
                    maxWidth = 200.dp,
                    minHeight = 120.dp,
                    maxHeight = 200.dp
                ),
            painter = painterResource(Res.drawable.missingno),
            contentDescription = "Missing Number reference",
            contentScale = ContentScale.Fit
        )
    } else {
        Text(
            text = stringResource(Res.string.score_result, score),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.padding))
        Text(
            text = stringResource(Res.string.with_card_pairs, amount),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameOverButtons(
    navController: NavController,
    isDarkMode: Boolean,
    amount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.smallerPadding)
    ) {
        if (amount != 0) {
            MemoryGameRedButton(
                text = stringResource(Res.string.play_again),
                optionSelectedSound = true,
                onClick = {
                    navController.navigate(AstorMemoryRoutes.Game(amount = amount))
                }
            )
        }

        MemoryGameWhiteButton(
            text = stringResource(Res.string.back),
            isDarkMode = isDarkMode,
            onClick = {
                navController.navigate(AstorMemoryRoutes.Menu) {
                    popUpTo<AstorMemoryRoutes.Menu> {
                        inclusive = true
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(Dimens.smallerPadding))
        Narcisus()
    }
}
