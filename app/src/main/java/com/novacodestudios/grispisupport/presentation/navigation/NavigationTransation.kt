package com.novacodestudios.grispisupport.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

private const val DURATION=700
fun enterTransition(): AnimatedContentTransitionScope<*>.() -> EnterTransition {
    return {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(DURATION)
        ) + fadeIn(animationSpec = tween(DURATION))
    }
}

fun exitTransition(): AnimatedContentTransitionScope<*>.() -> ExitTransition {
    return {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(DURATION)
        ) + fadeOut(animationSpec = tween(DURATION))
    }
}

fun popEnterTransition(): AnimatedContentTransitionScope<*>.() -> EnterTransition {
    return {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(DURATION)
        ) + fadeIn(animationSpec = tween(DURATION))
    }
}

fun popExitTransition(): AnimatedContentTransitionScope<*>.() -> ExitTransition {
    return {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(DURATION)
        ) + fadeOut(animationSpec = tween(DURATION))
    }
}