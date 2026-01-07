package io.lb.astormemory.game.ds.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.astor_game_logo
import astormemory.designsystem.generated.resources.narcisus
import io.lb.astormemory.game.ds.theme.Dimens
import io.lb.astormemory.game.ds.theme.LogoConstants
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemoryGameLogo(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .aspectRatio(LogoConstants.LOGO_ASPECT_RATIO)
            .heightIn(min = Dimens.logoMinHeight, max = Dimens.logoMaxHeight),
        painter = painterResource(Res.drawable.astor_game_logo),
        contentDescription = "Astor Memory Challenge",
        contentScale = ContentScale.Fit
    )
}

@Composable
fun Narcisus() {
    Image(
        modifier = Modifier
            .size(Dimens.narcisusIconSize)
            .padding(Dimens.narcisusIconPadding),
        painter = painterResource(Res.drawable.narcisus),
        contentDescription = stringResource(Res.string.narcisus),
        contentScale = ContentScale.Fit
    )
}
