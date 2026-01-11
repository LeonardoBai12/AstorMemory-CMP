package io.lb.astormemory.game.ds.effects

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally

object AstorMemoryTransitions {

    private const val TRANSITION_DURATION = 300

    val defaultEnterTransition: EnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

    val defaultExitTransition: ExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / 3 },
        animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

    val defaultPopEnterTransition: EnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / 3 },
        animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

    val defaultPopExitTransition: ExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

    val gameEnterTransition: EnterTransition = scaleIn(
        initialScale = 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    ) + fadeIn(animationSpec = tween(400))

    val gameExitTransition: ExitTransition = scaleOut(
        targetScale = 0.9f,
        animationSpec = tween(TRANSITION_DURATION)
    ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

    val gameOverEnterTransition: EnterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    ) + fadeIn(animationSpec = tween(500))
}