package io.lb.astormemory.game.ds.theme

import androidx.compose.ui.unit.dp

object Dimens {
    val smallPadding = 8.dp
    val smallerPadding = 12.dp
    val padding = 16.dp
    val largePadding = 24.dp
    val bigPadding = 32.dp
    val cardCorner = 8.dp
    val largeButtonHeight = 48.dp
    val buttonHeight = 48.dp
    val buttonCorner = 24.dp
    val logoHeight = 64.dp
    val logoAspectRatio = 4.5f.dp
    val narcissusSize = 120.dp
    val defaultElevation = 8.dp
    val pressedElevation = 16.dp

    val loadingIndicatorSize = 80.dp
    val loadingIconSize = 60.dp
    val loadingStrokeWidth = 5.dp

    val iconButtonSize = 48.dp
    val plusMinusButtonSize = 56.dp
    val minIconButtonSize = 48.dp
    val maxPlusMinusButtonSize = 72.dp
    val minButtonHeight = 56.dp
    val maxButtonHeight = 80.dp

    val cardPaddingLarge = 4.dp
    val cardPaddingSmall = 2.dp
    val cardBorderWidth = 2.dp

    val logoMinHeight = 60.dp
    val logoMaxHeight = 200.dp

    val narcisusIconSize = 48.dp
    val narcisusIconPadding = 4.dp
}

object LogoConstants {
    const val LOGO_ASPECT_RATIO = 3f
}

object ButtonConstants {
    const val BUTTON_HEIGHT_SCREEN_DIVISOR = 16
    const val BUTTON_WIDTH_FRACTION = 0.8f
    const val BUTTON_TEXT_SIZE = 24f
}

object LoadingConstants {
    const val INDICATOR_SCREEN_HEIGHT_DIVISOR = 6
    const val IMAGE_SCREEN_HEIGHT_DIVISOR = 8
}

object CardConstants {
    const val DEFAULT_CARDS_PER_LINE = 4
    const val DEFAULT_CARDS_PER_COLUMN = 6
    const val MAX_CARDS_FOR_LARGE_PADDING = 5

    const val FLIP_ANIMATION_DURATION = 400
    const val CARD_NOT_FLIPPED_ROTATION = 0f
    const val CARD_FLIPPED_ROTATION = 180f
    const val FLIP_THRESHOLD = 90f
    const val CAMERA_DISTANCE = 12f

    const val HEIGHT_DIVISOR_6_COLUMNS = 6.35f
    const val HEIGHT_DIVISOR_5_COLUMNS = 5.1f
    const val HEIGHT_DIVISOR_8_COLUMNS = 8.5f
    const val HEIGHT_DIVISOR_7_COLUMNS = 7.4f
    const val HEIGHT_DIVISOR_DEFAULT = 9.35f
}
