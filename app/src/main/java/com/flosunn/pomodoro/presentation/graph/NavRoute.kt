package com.flosunn.pomodoro.presentation.graph

import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.constants.DurationType
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {

    @Serializable
    data object Auth : NavRoute, NavKey

    @Serializable
    data object Swipe : NavRoute, NavKey

    @Serializable
    data object Preference : NavRoute, NavKey

    @Serializable
    data class ChooseDuration(
        val type: DurationType,
    ) : NavRoute, NavKey

    @Serializable
    data object Appearance : NavRoute, NavKey

    @Serializable
    data object NotificationSettings : NavRoute, NavKey

    @Serializable
    data object FullScreen : NavRoute, NavKey

    @Serializable
    data object Account : NavRoute, NavKey

}