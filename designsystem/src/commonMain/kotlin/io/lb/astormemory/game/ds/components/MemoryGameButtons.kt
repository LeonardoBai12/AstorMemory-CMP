package io.lb.astormemory.game.ds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.backbutton
import astormemory.designsystem.generated.resources.blackbutton
import astormemory.designsystem.generated.resources.bluebutton
import astormemory.designsystem.generated.resources.closebutton
import astormemory.designsystem.generated.resources.minusbutton_b
import astormemory.designsystem.generated.resources.minusbutton_w
import astormemory.designsystem.generated.resources.plusbutton_b
import astormemory.designsystem.generated.resources.plusbutton_w
import astormemory.designsystem.generated.resources.rebbutton
import astormemory.designsystem.generated.resources.refreshbutton
import astormemory.designsystem.generated.resources.whitebutton
import io.lb.astormemory.game.ds.screen.getScreenHeight
import io.lb.astormemory.game.ds.theme.AstorMemoryChallengeTheme
import io.lb.astormemory.game.ds.theme.ButtonConstants
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.game.ds.theme.PrimaryRed
import io.lb.astormemory.game.platform.audio.AudioPlayer
import io.lb.astormemory.game.platform.preferences.AppPreferences
import io.lb.astormemory.game.platform.utils.AstorMemoryAudio
import io.lb.astormemory.game.platform.utils.PreferencesKeys
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

private fun playClickSound(
    optionSelectedSound: Boolean = false,
    playSound: Boolean = true,
    audioPlayer: AudioPlayer,
    prefs: AppPreferences,
) {
    if (playSound) {
        val isMuted = prefs.getBoolean(PreferencesKeys.IS_MUTED, false)
        val isSoundEnabled = prefs.getBoolean(PreferencesKeys.IS_SOUND_EFFECTS_ENABLED, true)
        if (isSoundEnabled) {
            if (optionSelectedSound) {
                AstorMemoryAudio.playOptionSelectEffect(isMuted, audioPlayer)
                return
            }
            AstorMemoryAudio.playClickEffect(isMuted, audioPlayer)
        }
    }
}

@Composable
fun MemoryGameButtonWithBackground(
    modifier: Modifier = Modifier,
    backgroundDrawable: DrawableResource,
    text: String? = null,
    textColor: Color? = null,
    playSound: Boolean = true,
    optionSelectedSound: Boolean = false,
    onClick: () -> Unit
) {
    val audioPlayer: AudioPlayer = koinInject()
    val prefs: AppPreferences = koinInject()

    Box(
        modifier = modifier
            .clickable {
                playClickSound(
                    playSound = playSound,
                    optionSelectedSound = optionSelectedSound,
                    audioPlayer = audioPlayer,
                    prefs = prefs
                )
                onClick()
            }
            .defaultMinSize(minHeight = Dimens.buttonHeight),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resource = backgroundDrawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = if (text == null) ContentScale.Fit else ContentScale.FillBounds
        )

        if (text != null) {
            AstorText(
                text = text,
                color = textColor ?: MaterialTheme.colorScheme.onBackground,
                fontSize = with(LocalDensity.current) {
                    minOf(ButtonConstants.BUTTON_TEXT_SIZE, (ButtonConstants.BUTTON_TEXT_SIZE * fontScale)).sp
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = Dimens.smallerPadding,
                    vertical = Dimens.smallPadding
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MemoryGameRedButton(
    text: String,
    optionSelectedSound: Boolean = false,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    val screenHeight = getScreenHeight()
    val density = LocalDensity.current

    val buttonHeight = with(density) {
        val baseHeight = screenHeight / ButtonConstants.BUTTON_HEIGHT_SCREEN_DIVISOR
        val fontScaledHeight = baseHeight * fontScale
        minOf(fontScaledHeight, Dimens.maxButtonHeight).coerceAtLeast(Dimens.minButtonHeight)
    }

    MemoryGameButtonWithBackground(
        modifier = Modifier
            .fillMaxWidth(ButtonConstants.BUTTON_WIDTH_FRACTION)
            .height(buttonHeight),
        backgroundDrawable = Res.drawable.rebbutton,
        text = text,
        textColor = Color.White,
        optionSelectedSound = optionSelectedSound,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameStopButton(
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    MemoryGameButtonWithBackground(
        modifier = Modifier.size(Dimens.iconButtonSize),
        backgroundDrawable = Res.drawable.closebutton,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameRestartButton(
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    MemoryGameButtonWithBackground(
        modifier = Modifier.size(Dimens.iconButtonSize),
        backgroundDrawable = Res.drawable.refreshbutton,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameBackButton(
    modifier: Modifier = Modifier,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    MemoryGameButtonWithBackground(
        modifier = modifier.size(Dimens.iconButtonSize),
        backgroundDrawable = Res.drawable.backbutton,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGamePlusButton(
    isDarkMode: Boolean,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    val buttonSize = with(density) {
        val baseSize = Dimens.plusMinusButtonSize
        val fontScaledSize = baseSize * fontScale
        minOf(fontScaledSize, Dimens.maxPlusMinusButtonSize).coerceAtLeast(Dimens.minIconButtonSize)
    }

    MemoryGameButtonWithBackground(
        modifier = Modifier.size(buttonSize),
        backgroundDrawable = if (isDarkMode) Res.drawable.plusbutton_b else Res.drawable.plusbutton_w,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameMinusButton(
    isDarkMode: Boolean,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    val buttonSize = with(density) {
        val baseSize = Dimens.plusMinusButtonSize
        val fontScaledSize = baseSize * fontScale
        minOf(fontScaledSize, Dimens.maxPlusMinusButtonSize).coerceAtLeast(Dimens.minIconButtonSize)
    }

    MemoryGameButtonWithBackground(
        modifier = Modifier.size(buttonSize),
        backgroundDrawable = if (isDarkMode) Res.drawable.minusbutton_b else Res.drawable.minusbutton_w,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameBlueButton(
    text: String,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    val screenHeight = getScreenHeight()
    val density = LocalDensity.current

    val buttonHeight = with(density) {
        val baseHeight = screenHeight / ButtonConstants.BUTTON_HEIGHT_SCREEN_DIVISOR
        val fontScaledHeight = baseHeight * fontScale
        minOf(fontScaledHeight, Dimens.maxButtonHeight)
            .coerceAtLeast(Dimens.minButtonHeight)
    }

    MemoryGameButtonWithBackground(
        modifier = Modifier
            .fillMaxWidth(ButtonConstants.BUTTON_WIDTH_FRACTION)
            .height(buttonHeight),
        backgroundDrawable = Res.drawable.bluebutton,
        text = text,
        textColor = Color.White,
        playSound = playSound,
        onClick = onClick
    )
}

@Composable
fun MemoryGameWhiteButton(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    text: String,
    playSound: Boolean = true,
    onClick: () -> Unit
) {
    val screenHeight = getScreenHeight()
    val density = LocalDensity.current

    val buttonHeight = with(density) {
        val baseHeight = screenHeight / ButtonConstants.BUTTON_HEIGHT_SCREEN_DIVISOR
        val fontScaledHeight = baseHeight * fontScale
        minOf(fontScaledHeight, Dimens.maxButtonHeight).coerceAtLeast(Dimens.minButtonHeight)
    }

    MemoryGameButtonWithBackground(
        modifier = Modifier
            .fillMaxWidth(ButtonConstants.BUTTON_WIDTH_FRACTION)
            .height(buttonHeight),
        backgroundDrawable = if (isDarkMode) Res.drawable.blackbutton else Res.drawable.whitebutton,
        text = text,
        textColor = PrimaryRed,
        playSound = playSound,
        onClick = onClick
    )
}

@Preview(name = "Red Buttons - Normal Text", showBackground = true)
@Composable
fun RedButtonNormalPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemoryGameRedButton(
                text = "Start Game",
                onClick = { }
            )
            MemoryGameRedButton(
                text = "Play Again",
                onClick = { }
            )
            MemoryGameRedButton(
                text = "Try Again",
                onClick = { }
            )
        }
    }
}

@Preview(name = "Red Buttons - Long Text", showBackground = true)
@Composable
fun RedButtonLongTextPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemoryGameRedButton(
                text = "Start New Memory Challenge Game",
                onClick = { }
            )
            MemoryGameRedButton(
                text = "Continue Playing This Awesome Memory Game",
                onClick = { }
            )
            MemoryGameRedButton(
                text = "This is a very long button text that should test overflow handling",
                onClick = { }
            )
        }
    }
}

@Preview(name = "Blue Buttons - Various Text", showBackground = true)
@Composable
fun BlueButtonPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemoryGameBlueButton(
                text = "Highscores",
                onClick = { }
            )
            MemoryGameBlueButton(
                text = "View All High Scores",
                onClick = { }
            )
            MemoryGameBlueButton(
                text = "Check Out The Leaderboard And Rankings",
                onClick = { }
            )
        }
    }
}

@Preview(name = "White Buttons - Light Mode", showBackground = true)
@Composable
fun WhiteButtonLightPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Back",
                onClick = { }
            )
            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Go Back to Menu",
                onClick = { }
            )
            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Return to Main Menu Screen",
                onClick = { }
            )
            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Quit",
                onClick = { }
            )
        }
    }
}

@Preview(name = "White Buttons - Dark Mode", showBackground = true)
@Composable
fun WhiteButtonDarkPreview() {
    AstorMemoryChallengeTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemoryGameWhiteButton(
                isDarkMode = true,
                text = "Back",
                onClick = { }
            )
            MemoryGameWhiteButton(
                isDarkMode = true,
                text = "Go Back to Menu",
                onClick = { }
            )
            MemoryGameWhiteButton(
                isDarkMode = true,
                text = "Return to Main Menu Screen",
                onClick = { }
            )
        }
    }
}

@Preview(name = "Icon Buttons - Light Mode", showBackground = true)
@Composable
fun IconButtonsLightPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AstorText(
                text = "Icon Buttons",
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
            ) {
                MemoryGameStopButton(onClick = { })
                MemoryGameRestartButton(onClick = { })
                MemoryGameBackButton(onClick = { })
            }

            AstorText(
                text = "Plus/Minus Buttons",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
            ) {
                MemoryGameMinusButton(
                    isDarkMode = false,
                    onClick = { }
                )
                AstorText(
                    text = "5",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                MemoryGamePlusButton(
                    isDarkMode = false,
                    onClick = { }
                )
            }
        }
    }
}

@Preview(name = "Icon Buttons - Dark Mode", showBackground = true)
@Composable
fun IconButtonsDarkPreview() {
    AstorMemoryChallengeTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AstorText(
                text = "Icon Buttons - Dark",
                style = MaterialTheme.typography.headlineSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
            ) {
                MemoryGameStopButton(onClick = { })
                MemoryGameRestartButton(onClick = { })
                MemoryGameBackButton(onClick = { })
            }

            AstorText(
                text = "Plus/Minus Buttons - Dark",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
            ) {
                MemoryGameMinusButton(
                    isDarkMode = true,
                    onClick = { }
                )
                AstorText(
                    text = "12",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                MemoryGamePlusButton(
                    isDarkMode = true,
                    onClick = { }
                )
            }
        }
    }
}

@Preview(name = "Buttons - Small Screen", showBackground = true, widthDp = 320, heightDp = 480)
@Composable
fun ButtonsSmallScreenPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .padding(Dimens.padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AstorText(
                text = "Small Screen (320dp)",
                style = MaterialTheme.typography.titleMedium
            )

            MemoryGameRedButton(
                text = "Start",
                onClick = { }
            )

            MemoryGameRedButton(
                text = "Start New Game",
                onClick = { }
            )

            MemoryGameBlueButton(
                text = "Scores",
                onClick = { }
            )

            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Back",
                onClick = { }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MemoryGameStopButton(onClick = { })
                MemoryGameRestartButton(onClick = { })
                MemoryGameBackButton(onClick = { })
            }
        }
    }
}

@Preview(name = "Extreme Text Cases", showBackground = true)
@Composable
fun ButtonsExtremeTextPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .padding(Dimens.padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AstorText(
                text = "Extreme Text Length Tests",
                style = MaterialTheme.typography.headlineSmall
            )

            MemoryGameRedButton(
                text = "A",
                onClick = { }
            )

            MemoryGameRedButton(
                text = "This is an extremely long button text that should definitely test the" +
                    " overflow handling and ellipsis functionality",
                onClick = { }
            )

            MemoryGameBlueButton(
                text = "SUPERCALIFRAGILISTICEXPIALIDOCIOUS",
                onClick = { }
            )

            MemoryGameWhiteButton(
                isDarkMode = false,
                text = "Antidisestablishmentarianism",
                onClick = { }
            )

            MemoryGameRedButton(
                text = "🎮 Play Game 🎯",
                onClick = { }
            )
        }
    }
}

@Preview(name = "Button States Comparison", showBackground = true)
@Composable
fun ButtonStatesComparisonPreview() {
    AstorMemoryChallengeTheme(darkTheme = false) {
        Row(
            modifier = Modifier.padding(Dimens.padding),
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AstorText(
                    text = "Light Theme",
                    style = MaterialTheme.typography.titleMedium
                )
                MemoryGameRedButton(
                    text = "Red Button",
                    onClick = { }
                )
                MemoryGameBlueButton(
                    text = "Blue Button",
                    onClick = { }
                )
                MemoryGameWhiteButton(
                    isDarkMode = false,
                    text = "White Button",
                    onClick = { }
                )
                Row {
                    MemoryGameMinusButton(isDarkMode = false, onClick = { })
                    Spacer(modifier = Modifier.width(8.dp))
                    MemoryGamePlusButton(isDarkMode = false, onClick = { })
                }
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.Gray)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AstorText(
                    text = "Dark Theme",
                    style = MaterialTheme.typography.titleMedium
                )
                MemoryGameRedButton(
                    text = "Red Button",
                    onClick = { }
                )
                MemoryGameBlueButton(
                    text = "Blue Button",
                    onClick = { }
                )
                MemoryGameWhiteButton(
                    isDarkMode = true,
                    text = "Black Button",
                    onClick = { }
                )
                Row {
                    MemoryGameMinusButton(isDarkMode = true, onClick = { })
                    Spacer(modifier = Modifier.width(8.dp))
                    MemoryGamePlusButton(isDarkMode = true, onClick = { })
                }
            }
        }
    }
}
