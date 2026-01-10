package io.lb.astormemory.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import astormemory.composeapp.generated.resources.Res
import astormemory.composeapp.generated.resources.cards_height
import astormemory.composeapp.generated.resources.cards_width
import astormemory.composeapp.generated.resources.dark_mode
import astormemory.composeapp.generated.resources.dynamic_layout
import astormemory.composeapp.generated.resources.game_screen_layout
import astormemory.composeapp.generated.resources.preview
import astormemory.composeapp.generated.resources.sound_effects
import io.lb.astormemory.game.ds.components.MemoryGameBackButton
import io.lb.astormemory.game.ds.components.MemoryGameCard
import io.lb.astormemory.game.ds.components.VisualIntSelector
import io.lb.astormemory.game.ds.model.GameCard
import io.lb.astormemory.game.ds.screen.getScreenHeight
import io.lb.astormemory.game.ds.screen.getScreenWidth
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.shared.model.AstorCard
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    isDarkMode: Boolean,
    isDynamicLayout: Boolean,
    isSoundEffectsEnabled: Boolean,
    onChangeDarkMode: (Boolean) -> Unit,
    onChangeDynamicLayout: (Boolean) -> Unit,
    onChangeSoundEffectsEnabled: (Boolean) -> Unit,
    onChangeCardsPerLine: (Int) -> Unit,
    onChangeCardsPerColumn: (Int) -> Unit
) {
    val screenWidth = getScreenWidth()
    val selectedCardsPerLine = remember { mutableIntStateOf(cardsPerLine) }
    val selectedCardsPerColumn = remember { mutableIntStateOf(cardsPerColumn) }
    val darkMode = remember { mutableStateOf(isDarkMode) }
    val dynamicLayout = remember { mutableStateOf(isDynamicLayout) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            BackButton(navController)
            Spacer(modifier = Modifier.height(Dimens.largePadding))
            SettingsSwitch(
                title = stringResource(Res.string.dark_mode),
                value = darkMode,
                onChange = onChangeDarkMode
            )
            Spacer(modifier = Modifier.height(Dimens.bigPadding))
            SettingsSwitch(
                title = stringResource(Res.string.sound_effects),
                value = remember { mutableStateOf(isSoundEffectsEnabled) },
                onChange = onChangeSoundEffectsEnabled
            )

            Spacer(modifier = Modifier.height(Dimens.bigPadding))
            GameScreenLayoutText()
            Spacer(modifier = Modifier.height(Dimens.largePadding))
            SettingsSwitch(
                title = stringResource(Res.string.dynamic_layout),
                value = dynamicLayout,
                onChange = onChangeDynamicLayout
            )
            Spacer(modifier = Modifier.height(Dimens.largePadding))

            AnimatedVisibility(visible = dynamicLayout.value.not()) {
                Column {
                    val isSmallScreen = screenWidth < 400.dp
                    if (isSmallScreen) {
                        SmallScreenContent(
                            selectedCardsPerLine = selectedCardsPerLine,
                            darkMode = darkMode,
                            onChangeCardsPerLine = onChangeCardsPerLine,
                            selectedCardsPerColumn = selectedCardsPerColumn,
                            onChangeCardsPerColumn = onChangeCardsPerColumn
                        )
                    } else {
                        LargeScreenContent(
                            selectedCardsPerLine = selectedCardsPerLine,
                            darkMode = darkMode,
                            onChangeCardsPerLine = onChangeCardsPerLine,
                            selectedCardsPerColumn = selectedCardsPerColumn,
                            onChangeCardsPerColumn = onChangeCardsPerColumn
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.bigPadding))
                    Text(
                        text = stringResource(Res.string.preview),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.padding),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(Dimens.padding))
                    CardsPreview(
                        selectedCardsPerLine = selectedCardsPerLine.intValue,
                        selectedCardsPerColumn = selectedCardsPerColumn.intValue
                    )
                    Spacer(modifier = Modifier.height(Dimens.largePadding))
                }
            }
        }
    }
}

@Composable
private fun GameScreenLayoutText() {
    Text(
        text = stringResource(Res.string.game_screen_layout),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun LargeScreenContent(
    selectedCardsPerLine: MutableIntState,
    darkMode: MutableState<Boolean>,
    onChangeCardsPerLine: (Int) -> Unit,
    selectedCardsPerColumn: MutableIntState,
    onChangeCardsPerColumn: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardsPerLineContent(
                selectedCardsPerLine = selectedCardsPerLine,
                darkMode = darkMode,
                onChangeCardsPerLine = onChangeCardsPerLine
            )
        }
        Spacer(modifier = Modifier.width(Dimens.padding))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardsPerColumnContent(
                selectedCardsPerColumn = selectedCardsPerColumn,
                darkMode = darkMode,
                onChangeCardsPerColumn = onChangeCardsPerColumn
            )
        }
    }
}

@Composable
private fun SmallScreenContent(
    selectedCardsPerLine: MutableIntState,
    darkMode: MutableState<Boolean>,
    onChangeCardsPerLine: (Int) -> Unit,
    selectedCardsPerColumn: MutableIntState,
    onChangeCardsPerColumn: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardsPerLineContent(
                selectedCardsPerLine = selectedCardsPerLine,
                darkMode = darkMode,
                onChangeCardsPerLine = onChangeCardsPerLine
            )
        }
        Spacer(modifier = Modifier.height(Dimens.largePadding))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardsPerColumnContent(
                selectedCardsPerColumn = selectedCardsPerColumn,
                darkMode = darkMode,
                onChangeCardsPerColumn = onChangeCardsPerColumn
            )
        }
    }
}

@Composable
private fun CardsPerLineContent(
    selectedCardsPerLine: MutableIntState,
    darkMode: MutableState<Boolean>,
    onChangeCardsPerLine: (Int) -> Unit
) {
    Text(
        text = stringResource(Res.string.cards_width),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(Dimens.smallPadding))
    VisualIntSelector(
        intState = selectedCardsPerLine,
        minValue = 3,
        maxValue = 6,
        isDarkMode = darkMode.value,
        onChangeAmount = onChangeCardsPerLine,
        inverted = true
    )
}

@Composable
private fun CardsPerColumnContent(
    selectedCardsPerColumn: MutableIntState,
    darkMode: MutableState<Boolean>,
    onChangeCardsPerColumn: (Int) -> Unit
) {
    Text(
        text = stringResource(Res.string.cards_height),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(Dimens.smallPadding))
    VisualIntSelector(
        intState = selectedCardsPerColumn,
        minValue = 5,
        maxValue = 9,
        isDarkMode = darkMode.value,
        onChangeAmount = onChangeCardsPerColumn,
        inverted = true
    )
}

@Composable
private fun SettingsSwitch(
    title: String,
    value: MutableState<Boolean>,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Switch(
            checked = value.value,
            onCheckedChange = {
                value.value = it
                onChange(it)
            }
        )
    }
}

@Composable
private fun BackButton(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemoryGameBackButton(
            modifier = Modifier.padding(
                top = Dimens.padding,
                start = Dimens.padding
            ),
        ) {
            navController.navigateUp()
        }
    }
}

@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardsPreview(
    selectedCardsPerLine: Int,
    selectedCardsPerColumn: Int
) {
    val screenHeight = getScreenHeight()
    val maxPreviewItems = minOf(16, selectedCardsPerLine * 4)

    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = Dimens.padding)
            .heightIn(max = screenHeight * 0.6f),
        columns = GridCells.Fixed(selectedCardsPerLine),
        userScrollEnabled = false,
    ) {
        items(maxPreviewItems) { index ->
            MemoryGameCard(
                card = GameCard(
                    astorCard = AstorCard(
                        id = index.toString(),
                        astorId = index,
                        name = "Preview $index",
                        imageData = ByteArray(0),
                        imageUrl = ""
                    ),
                    isFlipped = false,
                    isMatched = false
                ),
                cardsPerLine = selectedCardsPerLine,
                cardsPerColumn = selectedCardsPerColumn
            ) {
                // No action in preview
            }
        }
    }
}
