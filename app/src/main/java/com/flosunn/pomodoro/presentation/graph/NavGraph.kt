package com.flosunn.pomodoro.presentation.graph

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.flosunn.pomodoro.presentation.auth.AuthView
import com.flosunn.pomodoro.presentation.swipe.SwipeView
import com.flosunn.pomodoro.ui.components.shared.NotFoundView
import com.flosunn.pomodoro.ui.theme.AppTheme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoute.Auth::class, NavRoute.Auth.serializer())
                    subclass(NavRoute.Swipe::class, NavRoute.Swipe.serializer())
                }
            }
        },
        NavRoute.Swipe
    )

    NavDisplay(
        modifier = modifier.background(AppTheme.colors.backgroundColor),
        backStack = navBackStack,
        onBack = { navBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = { key ->
            when (key) {
                is NavRoute.Auth -> NavEntry(key) { AuthView(navBackStack) }
                is NavRoute.Swipe -> NavEntry(key) { SwipeView(navBackStack) }
                else -> NavEntry(key) { NotFoundView() }
            }
        }
    )
}