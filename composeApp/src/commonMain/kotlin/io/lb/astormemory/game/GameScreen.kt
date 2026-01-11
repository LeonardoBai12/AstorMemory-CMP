package io.lb.astormemory.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import astormemory.composeapp.generated.resources.Res
import astormemory.composeapp.generated.resources.combo_bonus
import astormemory.composeapp.generated.resources.ops_something_went_wrong
import astormemory.composeapp.generated.resources.try_again
import io.lb.astormemory.game.ds.components.LoadingIndicator
import io.lb.astormemory.game.ds.components.MemoryGameCard
import io.lb.astormemory.game.ds.components.MemoryGameRedButton
import io.lb.astormemory.game.ds.components.MemoryGameRestartButton
import io.lb.astormemory.game.ds.components.MemoryGameStopButton
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.utils.AstorMemoryAudio
import io.lb.astormemory.navigation.AstorMemoryRoutes
import io.lb.presentation.game.GameEvent
import io.lb.presentation.game.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

private const val CLICK_LOCK_DELAY = 100L

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun GameScreen(
    navController: NavController,
    state: GameState,
    eventFlow: Flow<GameViewModel.UiEvent>,
    onEvent: (GameEvent) -> Unit,
    isDarkMode: Boolean,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    onCardFlipped: () -> Unit,
    onCardMatched: (Int) -> Unit
) {
    val lastSelectedCard = remember { mutableStateOf("") }
    var restartTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = "GameScreen") {
        eventFlow.collectLatest { event ->
            when (event) {
                is GameViewModel.UiEvent.Finish -> {
                    navController.navigate(
                        AstorMemoryRoutes.GameOver(
                            score = event.score,
                            amount = state.amount
                        )
                    ) {
                        popUpTo<AstorMemoryRoutes.Game> {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            GameTopBar(
                navController = navController,
                state = state,
                onEvent = onEvent,
                lastSelectedCard = lastSelectedCard,
                isDarkMode = isDarkMode,
                onRestart = { restartTrigger++ }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
            !state.message.isNullOrEmpty() && state.message != "null" || state.cards.isEmpty() -> {
                ErrorMessage(
                    padding = padding,
                    state = state,
                    onEvent = onEvent
                )
            }
            else -> {
                CardGrid(
                    onEvent = onEvent,
                    padding = padding,
                    state = state,
                    lastSelectedCard = lastSelectedCard,
                    cardsPerLine = cardsPerLine,
                    cardsPerColumn = cardsPerColumn,
                    onCardFlipped = onCardFlipped,
                    onCardMatched = {
                        onCardMatched(state.cards.count { it.isMatched })
                    },
                    restartTrigger = restartTrigger
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameTopBar(
    navController: NavController,
    state: GameState,
    onEvent: (GameEvent) -> Unit,
    lastSelectedCard: MutableState<String>,
    isDarkMode: Boolean,
    onRestart: () -> Unit
) {
    val audioPlayer: AudioPlayer = koinInject()
    TopAppBar(
        modifier = Modifier.padding(top = Dimens.smallerPadding),
        title = {
            Column(verticalArrangement = Arrangement.Top) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = Dimens.smallerPadding)
                        .fillMaxWidth(),
                    text = if (!state.isLoading && state.score > 0) {
                        "${state.score} pts"
                    } else {
                        ""
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.End
                )
                ComboContent(state, isDarkMode)
            }
        },
        navigationIcon = {
            Row {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .widthIn(max = 48.dp)
                ) {
                    MemoryGameStopButton {
                        AstorMemoryAudio.stopShuffleEffect(audioPlayer)
                        navController.navigate(AstorMemoryRoutes.Menu) {
                            popUpTo<AstorMemoryRoutes.Menu> {
                                inclusive = true
                            }
                        }
                    }
                }
                if (!state.isLoading && state.message.isNullOrEmpty() &&
                    state.cards.any { !it.isMatched }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .widthIn(max = 48.dp)
                    ) {
                        MemoryGameRestartButton {
                            AstorMemoryAudio.stopShuffleEffect(audioPlayer)
                            lastSelectedCard.value = ""
                            onEvent(GameEvent.GameRestarted)
                            onRestart()
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun ComboContent(state: GameState, isDarkMode: Boolean) {
    if (state.currentCombo > 1) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.smallerPadding)
                .fillMaxWidth(),
            text = stringResource(Res.string.combo_bonus, (state.currentCombo) * 10),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.End,
            color = topBarTextColor(isDarkMode)
        )
    } else {
        Spacer(modifier = Modifier.height(Dimens.largePadding))
    }
}

@Composable
private fun ErrorMessage(
    padding: PaddingValues,
    state: GameState,
    onEvent: (GameEvent) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(padding)
            .padding(Dimens.padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.message.takeIf {
                    !it.isNullOrEmpty() && it != "null"
                } ?: stringResource(Res.string.ops_something_went_wrong),
                fontWeight = FontWeight.W600,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.largePadding))
            MemoryGameRedButton(
                text = stringResource(Res.string.try_again),
                onClick = {
                    onEvent(GameEvent.OnRequestGames)
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardGrid(
    onEvent: (GameEvent) -> Unit,
    padding: PaddingValues,
    state: GameState,
    lastSelectedCard: MutableState<String>,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    onCardFlipped: () -> Unit,
    onCardMatched: () -> Unit,
    restartTrigger: Int = 0
) {
    val clickLock = remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.cards.isNotEmpty(), restartTrigger) {
        if (state.cards.isNotEmpty()) {
            isVisible = false
            delay(50)
            isVisible = true
        }
    }

    LazyVerticalGrid(
        modifier = Modifier
            .padding(padding)
            .padding(top = Dimens.smallerPadding)
            .padding(horizontal = Dimens.smallerPadding),
        columns = GridCells.Fixed(cardsPerLine),
        userScrollEnabled = true,
        contentPadding = PaddingValues(bottom = Dimens.padding)
    ) {
        items(
            count = state.cards.size,
            key = { index -> index }
        ) { index ->
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = index * 50,
                        easing = EaseOutBounce
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = index * 50
                    )
                ),
                modifier = Modifier.animateItem()
            ) {
                GameCard(
                    state = state,
                    index = index,
                    cardsPerLine = cardsPerLine,
                    cardsPerColumn = cardsPerColumn,
                    clickLock = clickLock,
                    onCardFlipped = onCardFlipped,
                    lastSelectedCard = lastSelectedCard,
                    onEvent = onEvent,
                    onCardMatched = onCardMatched
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GameCard(
    state: GameState,
    index: Int,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    clickLock: MutableState<Boolean>,
    onCardFlipped: () -> Unit,
    lastSelectedCard: MutableState<String>,
    onEvent: (GameEvent) -> Unit,
    onCardMatched: () -> Unit
) {
    MemoryGameCard(
        card = state.cards[index],
        cardsPerLine = cardsPerLine,
        cardsPerColumn = cardsPerColumn
    ) {
        if (clickLock.value) return@MemoryGameCard

        clickLock.value = true

        if (state.cards[index].isFlipped || state.cards[index].isMatched) {
            clickLock.value = false
            return@MemoryGameCard
        }

        if (state.cards.count { it.isFlipped && !it.isMatched } >= 2) {
            clickLock.value = false
            return@MemoryGameCard
        }

        onCardFlipped()
        afterCardFlipped(
            lastSelectedCard = lastSelectedCard,
            state = state,
            index = index,
            onEvent = onEvent,
            onCardMatched = onCardMatched
        )
        resetClickLock(clickLock)
    }
}

private fun afterCardFlipped(
    lastSelectedCard: MutableState<String>,
    state: GameState,
    index: Int,
    onEvent: (GameEvent) -> Unit,
    onCardMatched: () -> Unit
) {
    when {
        lastSelectedCard.value.isEmpty() -> {
            lastSelectedCard.value = state.cards[index].astorCard.name
            onEvent(GameEvent.CardFlipped(index))
        }
        lastSelectedCard.value != state.cards[index].astorCard.name -> {
            onEvent(GameEvent.CardFlipped(index))
            onEvent(GameEvent.CardMismatched)
            lastSelectedCard.value = ""
        }
        else -> {
            onEvent(GameEvent.CardFlipped(index))
            onEvent(GameEvent.CardMatched(id = state.cards[index].astorCard.astorId))
            lastSelectedCard.value = ""
            onCardMatched()
        }
    }
}

private fun resetClickLock(clickLock: MutableState<Boolean>) {
    CoroutineScope(Dispatchers.Main).launch {
        delay(CLICK_LOCK_DELAY)
        clickLock.value = false
    }
}

@Composable
private fun topBarTextColor(isDarkMode: Boolean) = if (isDarkMode) {
    Color.Yellow
} else {
    Color.DarkGray
}
