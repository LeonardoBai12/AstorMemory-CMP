package io.lb.astormemory.game.ds.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.narcisus
import io.lb.astormemory.game.ds.model.GameCard
import io.lb.astormemory.game.ds.screen.getScreenHeight
import io.lb.astormemory.game.ds.theme.CardConstants
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.game.ds.theme.PrimaryRed
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.painterResource

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun MemoryGameCard(
    card: GameCard,
    cardsPerLine: Int = CardConstants.DEFAULT_CARDS_PER_LINE,
    cardsPerColumn: Int = CardConstants.DEFAULT_CARDS_PER_COLUMN,
    onClick: () -> Unit
) {
    val flipRotation by animateFloatAsState(
        targetValue = if (card.isFlipped) CardConstants.CARD_FLIPPED_ROTATION else CardConstants.CARD_NOT_FLIPPED_ROTATION,
        animationSpec = tween(
            durationMillis = CardConstants.FLIP_ANIMATION_DURATION,
            easing = EaseInOutCubic
        ),
        label = "cardFlip"
    )

    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .graphicsLayer {
                rotationY = flipRotation
                cameraDistance = CardConstants.CAMERA_DISTANCE * density
            }
    ) {
        if (flipRotation <= CardConstants.FLIP_THRESHOLD) {
            NotFlippedCard(cardsPerLine, cardsPerColumn, onClick)
        } else {
            val border = if (card.isMatched) {
                BorderStroke(Dimens.cardBorderWidth, Color.Green)
            } else {
                BorderStroke(Dimens.cardBorderWidth, PrimaryRed)
            }

            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = CardConstants.CARD_FLIPPED_ROTATION
                }
            ) {
                FlippedCard(border, cardsPerLine, cardsPerColumn, card)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun NotFlippedCard(
    cardsPerLine: Int,
    cardsPerColumn: Int,
    onClick: () -> Unit
) {
    val screenHeight = getScreenHeight()

    Card(
        modifier = Modifier
            .heightIn(max = getCardHeight(cardsPerColumn, screenHeight))
            .padding(
                getCardPadding(cardsPerLine)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.defaultElevation,
            pressedElevation = Dimens.pressedElevation
        ),
        onClick = {
            onClick()
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.narcisus),
                contentDescription = "Narcisus",
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun getCardPadding(cardsPerLine: Int) = if (cardsPerLine <= CardConstants.MAX_CARDS_FOR_LARGE_PADDING) {
    Dimens.cardPaddingLarge
} else {
    Dimens.cardPaddingSmall
}

@Composable
private fun getCardHeight(cardsPerColumn: Int, screenHeight: Dp) = when (cardsPerColumn) {
    6 -> screenHeight / CardConstants.HEIGHT_DIVISOR_6_COLUMNS
    5 -> screenHeight / CardConstants.HEIGHT_DIVISOR_5_COLUMNS
    8 -> screenHeight / CardConstants.HEIGHT_DIVISOR_8_COLUMNS
    7 -> screenHeight / CardConstants.HEIGHT_DIVISOR_7_COLUMNS
    else -> screenHeight / CardConstants.HEIGHT_DIVISOR_DEFAULT
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
private fun FlippedCard(
    border: BorderStroke,
    cardsPerLine: Int,
    cardsPerColumn: Int,
    card: GameCard
) {
    val screenHeight = getScreenHeight()

    Card(
        modifier = Modifier
            .heightIn(max = getCardHeight(cardsPerColumn, screenHeight))
            .padding(
                getCardPadding(cardsPerLine)
            )
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {},
                onLongClick = {},
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
        border = border,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imageBitmap = remember(card.astorCard.imageData) {
                card.astorCard.imageData.decodeToImageBitmap()
            }

            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                bitmap = imageBitmap,
                contentDescription = "Astor Flipped Card"
            )
        }
    }
}
