package io.lb.astormemory.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import astormemory.composeapp.generated.resources.Res
import astormemory.composeapp.generated.resources.amount_of_card_pairs
import astormemory.composeapp.generated.resources.highscores
import astormemory.composeapp.generated.resources.music_off
import astormemory.composeapp.generated.resources.music_on
import astormemory.composeapp.generated.resources.quit
import astormemory.composeapp.generated.resources.settings
import astormemory.composeapp.generated.resources.start_game
import astormemory.composeapp.generated.resources.the_more_cards_you_play_with
import io.lb.astormemory.game.ds.components.IntSelector
import io.lb.astormemory.game.ds.components.MemoryGameBlueButton
import io.lb.astormemory.game.ds.components.MemoryGameLogo
import io.lb.astormemory.game.ds.components.MemoryGameRedButton
import io.lb.astormemory.game.ds.components.MemoryGameWhiteButton
import io.lb.astormemory.game.ds.components.Narcisus
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.navigation.AstorMemoryRoutes
import io.lb.astormemory.theme.AstorMemoryDimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MenuScreen(
    navController: NavController,
    isDarkMode: Boolean,
    initialAmount: Int,
    isMuted: Boolean,
    onChangeMuted: (Boolean) -> Unit,
    onClickQuit: () -> Unit,
    onChangeAmount: (Int) -> Unit
) {
    var muted by remember { mutableStateOf(isMuted) }
    val amount = remember { mutableIntStateOf(initialAmount) }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            MenuTopIcons(
                muted = muted,
                onChangeMuted = {
                    muted = it
                    onChangeMuted(it)
                },
                onSettingsClick = {
                    navController.navigate(AstorMemoryRoutes.Settings)
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Dimens.largePadding))
                MemoryGameLogo(
                    Modifier
                        .fillMaxWidth(AstorMemoryDimens.menuLogoWidthFraction)
                        .heightIn(
                            min = AstorMemoryDimens.menuLogoMinHeight,
                            max = AstorMemoryDimens.menuLogoMaxHeight
                        )
                )
                PairsAmountSelector(
                    amount = amount,
                    isDarkMode = isDarkMode,
                    onChangeAmount = {
                        amount.value = it
                        onChangeAmount(it)
                    }
                )
                Text(
                    modifier = Modifier.fillMaxWidth(AstorMemoryDimens.menuTextWidthFraction),
                    text = stringResource(Res.string.the_more_cards_you_play_with),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(Dimens.bigPadding))
                ButtonsColumn(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    amount = amount.value,
                    onClickQuit = onClickQuit
                )
                Spacer(modifier = Modifier.height(Dimens.largePadding))
            }
        }
    }
}

@Composable
private fun PairsAmountSelector(
    amount: MutableIntState,
    isDarkMode: Boolean,
    onChangeAmount: (Int) -> Unit
) {
    Spacer(modifier = Modifier.height(Dimens.bigPadding))
    Text(
        text = stringResource(Res.string.amount_of_card_pairs),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(Dimens.padding))
    IntSelector(
        intState = amount,
        minValue = 1,
        maxValue = 30,
        isDarkMode = isDarkMode,
        onChangeAmount = onChangeAmount
    )
    Spacer(modifier = Modifier.height(Dimens.padding))
}

@Composable
private fun MenuTopIcons(
    muted: Boolean,
    onChangeMuted: (Boolean) -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding)
            .padding(top = Dimens.padding),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val iconSize = Dimens.bigPadding

        IconButton(
            onClick = { onChangeMuted(!muted) },
            modifier = Modifier.size(Dimens.largeButtonHeight)
        ) {
            Icon(
                painter = painterResource(
                    if (muted) Res.drawable.music_off
                    else Res.drawable.music_on
                ),
                contentDescription = "Muted or not",
                tint = Color.Gray,
                modifier = Modifier.size(iconSize)
            )
        }
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(Dimens.largeButtonHeight)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(Res.string.settings),
                tint = Color.Gray,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Composable
private fun ButtonsColumn(
    navController: NavController,
    isDarkMode: Boolean,
    amount: Int,
    onClickQuit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.padding)
    ) {
        MemoryGameRedButton(
            text = stringResource(Res.string.start_game),
            onClick = {
                navController.navigate(AstorMemoryRoutes.Game(amount = amount))

            }
        )

        MemoryGameBlueButton(
            text = stringResource(Res.string.highscores),
            onClick = {
                navController.navigate(AstorMemoryRoutes.HighScores)
            }
        )

        MemoryGameWhiteButton(
            isDarkMode = isDarkMode,
            text = stringResource(Res.string.quit),
            onClick = onClickQuit
        )

        Spacer(modifier = Modifier.height(Dimens.smallPadding))
        Narcisus()
    }
}

@Preview
@Composable
internal fun MenuScreenPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        MenuScreen(
            navController = rememberNavController(),
            isDarkMode = false,
            initialAmount = 5,
            isMuted = false,
            onChangeMuted = { },
            onClickQuit = { },
            onChangeAmount = { }
        )
    }
}

@Preview
@Composable
internal fun MenuScreenDarkPreview() {
    AstorMemoryChallengeTheme(darkTheme = true) {
        MenuScreen(
            navController = rememberNavController(),
            isDarkMode = true,
            initialAmount = 10,
            isMuted = true,
            onChangeMuted = { },
            onClickQuit = { },
            onChangeAmount = { }
        )
    }
}
