package io.lb.astormemory.game.ds.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = Color.White,
    secondary = PrimaryBlue,
    onSecondary = Color.White,
    tertiary = Color.White,
    onTertiary = PrimaryRed,
    onPrimaryContainer = DarkerBlue,
    onSecondaryContainer = DarkerRed,
    onTertiaryContainer = GrayBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryRedDark,
    onPrimary = Color.White,
    secondary = PrimaryBlueDark,
    onSecondary = Color.White,
    tertiary = Color.Black,
    onTertiary = PrimaryRed,
    onPrimaryContainer = DarkerBlueDark,
    onSecondaryContainer = DarkerRedDark,
    onTertiaryContainer = GrayBlueDark
)

@Composable
fun AstorMemoryChallengeTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    SetStatusBarColor(darkTheme)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
expect fun SetStatusBarColor(darkTheme: Boolean)
