package io.lb.astormemory.game.ds.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.lb.astormemory.game.ds.theme.Dimens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VisualIntSelector(
    intState: MutableIntState,
    minValue: Int,
    maxValue: Int,
    isDarkMode: Boolean,
    inverted: Boolean = false,
    onChangeAmount: (Int) -> Unit = {}
) {
    val totalBars = maxValue - minValue + 1

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        MemoryGameMinusButton(isDarkMode) {
            if (inverted) {
                if (intState.intValue < maxValue) {
                    intState.intValue++
                }
            } else {
                if (intState.intValue > minValue) {
                    intState.intValue--
                }
            }
            onChangeAmount(intState.intValue)
        }

        Spacer(modifier = Modifier.width(Dimens.smallerPadding))

        VisualBars(
            currentValue = intState.intValue,
            minValue = minValue,
            totalBars = totalBars,
            maxValue = maxValue,
            inverted = inverted
        )

        Spacer(modifier = Modifier.width(Dimens.smallerPadding))

        MemoryGamePlusButton(isDarkMode) {
            if (inverted) {
                if (intState.intValue > minValue) {
                    intState.intValue--
                }
            } else {
                if (intState.intValue < maxValue) {
                    intState.intValue++
                }
            }
            onChangeAmount(intState.intValue)
        }
    }
}

@Composable
private fun VisualBars(
    currentValue: Int,
    minValue: Int,
    maxValue: Int,
    totalBars: Int,
    inverted: Boolean
) {
    val activeBars = if (inverted) {
        maxValue - currentValue
    } else {
        currentValue - minValue
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.tinyPadding),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(totalBars) { index ->
            val isActive = index <= activeBars

            Bar(
                isActive = isActive,
                height = if (isActive) Dimens.bigPadding else Dimens.bigPadding * 0.4f,
                width = Dimens.tinyPadding
            )
        }
    }
}

@Composable
private fun Bar(
    isActive: Boolean,
    height: Dp,
    width: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(Dimens.tinyPadding))
            .background(
                if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                }
            )
    )
}

@Composable
fun IntSelector(
    intState: MutableIntState,
    minValue: Int,
    maxValue: Int,
    textSize: Int = 72,
    spaceBetween: Int = 24,
    isDarkMode: Boolean,
    onChangeAmount: (Int) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemoryGameMinusButton(isDarkMode) {
            if (intState.intValue > minValue) {
                intState.intValue--
            }
            onChangeAmount(intState.intValue)
        }
        Spacer(modifier = Modifier.width(spaceBetween.dp))
        Text(
            text = intState.intValue.toString(),
            fontSize = textSize.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.width(spaceBetween.dp))
        MemoryGamePlusButton(isDarkMode) {
            if (intState.intValue < maxValue) {
                intState.intValue++
            }
            onChangeAmount(intState.intValue)
        }
    }
}

@Preview
@Composable
fun VisualIntSelectorPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            val state1 = remember { mutableIntStateOf(3) }
            VisualIntSelector(
                intState = state1,
                minValue = 1,
                maxValue = 10,
                isDarkMode = false
            )

            val state2 = remember { mutableIntStateOf(5) }
            VisualIntSelector(
                intState = state2,
                minValue = 1,
                maxValue = 10,
                isDarkMode = false
            )
        }
    }
}

@Preview
@Composable
fun VisualIntSelectorInvertedPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            val state = remember { mutableIntStateOf(7) }
            VisualIntSelector(
                intState = state,
                minValue = 1,
                maxValue = 10,
                isDarkMode = false,
                inverted = true
            )
        }
    }
}

@Preview
@Composable
fun VisualIntSelectorDarkPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Dimens.padding),
            verticalArrangement = Arrangement.spacedBy(Dimens.largePadding)
        ) {
            val state1 = remember { mutableIntStateOf(2) }
            VisualIntSelector(
                intState = state1,
                minValue = 1,
                maxValue = 10,
                isDarkMode = true
            )

            val state2 = remember { mutableIntStateOf(8) }
            VisualIntSelector(
                intState = state2,
                minValue = 1,
                maxValue = 10,
                isDarkMode = true
            )
        }
    }
}