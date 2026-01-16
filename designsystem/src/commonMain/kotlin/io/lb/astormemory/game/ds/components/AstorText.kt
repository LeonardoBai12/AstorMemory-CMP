package io.lb.astormemory.game.ds.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import astormemory.designsystem.generated.resources.Res
import astormemory.designsystem.generated.resources.pixellari
import org.jetbrains.compose.resources.Font

@Composable
fun AstorText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = TextStyle(
        fontFamily = FontFamily(Font(Res.font.pixellari)),
    ),
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        overflow = overflow,
        textAlign = textAlign,
        style = style,
        maxLines = maxLines,
    )
}