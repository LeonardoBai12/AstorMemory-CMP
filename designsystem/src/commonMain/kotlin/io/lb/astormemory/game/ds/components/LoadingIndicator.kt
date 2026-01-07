package io.lb.astormemory.game.ds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.narcisus
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.game.ds.theme.LoadingConstants
import io.lb.astormemory.game.ds.theme.PrimaryRed

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource

import org.jetbrains.compose.resources.painterResource

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier.fillMaxSize(),
    screenHeight: Dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(screenHeight / LoadingConstants.INDICATOR_SCREEN_HEIGHT_DIVISOR),
            color = PrimaryRed,
            strokeWidth = Dimens.loadingStrokeWidth,
        )
        Image(
            modifier = Modifier.size(screenHeight / LoadingConstants.IMAGE_SCREEN_HEIGHT_DIVISOR),
            painter = painterResource(Res.drawable.narcisus),
            contentDescription = "Loading",
        )
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(Dimens.loadingIndicatorSize),
            color = PrimaryRed,
            strokeWidth = Dimens.loadingStrokeWidth,
        )
        Image(
            modifier = Modifier.size(Dimens.loadingIconSize),
            painter = painterResource(Res.drawable.narcisus),
            contentDescription = "Loading",
            contentScale = ContentScale.Fit
        )
    }
}
