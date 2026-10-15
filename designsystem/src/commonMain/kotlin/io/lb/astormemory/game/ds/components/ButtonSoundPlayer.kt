package io.lb.astormemory.game.ds.components

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Plays the sound of the design system buttons, so they don't need to know how audio works.
 */
interface ButtonSoundPlayer {
    /**
     * Play the sound of a button being clicked.
     */
    fun playClick()

    /**
     * Play the sound of an option being selected.
     */
    fun playOptionSelected()
}

private object SilentButtonSoundPlayer : ButtonSoundPlayer {
    override fun playClick() = Unit
    override fun playOptionSelected() = Unit
}

/**
 * The [ButtonSoundPlayer] used by the design system buttons. Silent unless the app provides one,
 * which keeps previews working without audio.
 */
val LocalButtonSoundPlayer = staticCompositionLocalOf<ButtonSoundPlayer> { SilentButtonSoundPlayer }
