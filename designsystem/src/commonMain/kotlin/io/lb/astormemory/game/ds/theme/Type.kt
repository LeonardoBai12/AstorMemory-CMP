package io.lb.astormemory.game.ds.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.ariblk
import org.jetbrains.compose.resources.Font

val Typography
    @Composable
    get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily(Font(Res.font.ariblk)),
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily(Font(Res.font.ariblk)),
            fontSize = 24.sp

        )
    )
