package io.lb.astormemory.highscore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import astormemory.composeapp.generated.resources.Res
import astormemory.composeapp.generated.resources.all
import astormemory.composeapp.generated.resources.back
import astormemory.composeapp.generated.resources.cards
import astormemory.composeapp.generated.resources.filter
import io.lb.astormemory.game.ds.components.AstorText
import io.lb.astormemory.game.ds.components.LoadingIndicator
import io.lb.astormemory.game.ds.components.MemoryGameLogo
import io.lb.astormemory.game.ds.components.MemoryGameWhiteButton
import io.lb.astormemory.game.ds.components.Narcisus
import io.lb.astormemory.game.ds.screen.getScreenHeight
import io.lb.astormemory.game.ds.theme.DarkerRed
import io.lb.astormemory.game.ds.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HighScoresScreen(
    navController: NavController,
    state: ScoreState,
    onEvent: (ScoresEvent) -> Unit,
    isDarkMode: Boolean
) {
    val selectedFilter = remember { mutableIntStateOf(0) }
    val dropDownMenuExpanded = remember { mutableStateOf(false) }
    val screenHeight = getScreenHeight()

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

            Spacer(modifier = Modifier.height(Dimens.largePadding))
            Filter(
                dropDownMenuExpanded = dropDownMenuExpanded,
                selectedFilter = selectedFilter,
                state = state,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(Dimens.largePadding))
            ScoreContent(state, screenHeight)
            Spacer(modifier = Modifier.weight(1f))

            MemoryGameWhiteButton(
                text = stringResource(Res.string.back),
                isDarkMode = isDarkMode,
                onClick = {
                    navController.navigateUp()
                }
            )

            Spacer(modifier = Modifier.height(Dimens.padding))
            Narcisus()
            Spacer(modifier = Modifier.height(Dimens.largePadding))
        }
    }
}

@Composable
private fun ScoreContent(
    state: ScoreState,
    screenHeight: Dp
) {
    when {
        state.isLoading -> {
            LoadingIndicator(
                modifier = Modifier.size(120.dp),
                screenHeight = screenHeight
            )
        }
        !state.message.isNullOrEmpty() -> {
            AstorText(
                text = state.message,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Dimens.padding)
            )
        }
        else -> {
            ScoresColumn(
                state = state,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Filter(
    dropDownMenuExpanded: MutableState<Boolean>,
    selectedFilter: MutableIntState,
    state: ScoreState,
    onEvent: (ScoresEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AstorText(
            text = stringResource(Res.string.filter),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(Dimens.padding))
        FilterMenu(
            dropDownMenuExpanded = dropDownMenuExpanded,
            selectedFilter = selectedFilter,
            state = state
        ) { filter ->
            dropDownMenuExpanded.value = false
            selectedFilter.intValue = filter
            onEvent(ScoresEvent.OnRequestScores(amount = filter))
        }
    }
}

@Composable
private fun ScoresColumn(
    state: ScoreState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.scores.forEachIndexed { index, score ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AstorText(
                    text = "${index + 1}.",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 32.dp)
                )
                Spacer(modifier = Modifier.width(Dimens.padding))
                AstorText(
                    text = score.score.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(Dimens.smallPadding))
                AstorText(
                    text = "(${score.amount} ${stringResource(Res.string.cards)})",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FilterMenu(
    dropDownMenuExpanded: MutableState<Boolean>,
    selectedFilter: MutableIntState,
    state: ScoreState,
    onClickMenuItem: (Int) -> Unit
) {
    Box {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkerRed,
                contentColor = Color.White
            ),
            onClick = { dropDownMenuExpanded.value = true },
        ) {
            AstorText(
                text = if (selectedFilter.intValue == 0) {
                    stringResource(Res.string.all)
                } else {
                    "${selectedFilter.intValue} ${stringResource(Res.string.cards)}"
                },
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        DropdownMenu(
            expanded = dropDownMenuExpanded.value,
            onDismissRequest = { dropDownMenuExpanded.value = false }
        ) {
            state.filters.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        AstorText(
                            text = if (filter == 0) {
                                stringResource(Res.string.all)
                            } else {
                                "$filter ${stringResource(Res.string.cards)}"
                            },
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selectedFilter.intValue == filter) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    onClick = {
                        onClickMenuItem(filter)
                    }
                )
            }
        }
    }
}
