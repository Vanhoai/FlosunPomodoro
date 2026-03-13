package com.flosunn.pomodoro.presentation.graph

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {

    @Serializable
    data object Auth : NavRoute, NavKey

    @Serializable
    data object Swipe : NavRoute, NavKey

}