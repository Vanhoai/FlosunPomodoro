package com.flosunn.pomodoro.presentation.graph

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.flosunn.pomodoro.core.constants.DurationType
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
sealed interface NavRoute : NavKey {

    @Serializable
    data object Encrypt : NavRoute, NavKey

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
    data object BiometricAuthentication : NavRoute, NavKey

    @Serializable
    data object NotificationSettings : NavRoute, NavKey

    @Serializable
    data object FullScreen : NavRoute, NavKey

    @Serializable
    data object Account : NavRoute, NavKey


    @Serializable
    data object AppLanguage : NavRoute, NavKey

    @Serializable
    data object TwelveWeekYear : NavRoute, NavKey

    @Serializable
    data object AddNewYear : NavRoute, NavKey

    @Serializable
    data object YearDetail : NavRoute, NavKey

    @Serializable
    data object WeekDetail : NavRoute, NavKey

    @Serializable
    data object DatePicker : NavRoute, NavKey

    @Serializable
    data object UpdateGoal : NavRoute, NavKey

    @Serializable
    data object AddGoal : NavRoute, NavKey
}

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(NavRoute.Auth::class, NavRoute.Auth.serializer())
            subclass(NavRoute.Swipe::class, NavRoute.Swipe.serializer())
            subclass(
                subclass = NavRoute.Preference::class,
                serializer = NavRoute.Preference.serializer()
            )
            subclass(
                subclass = NavRoute.ChooseDuration::class,
                serializer = NavRoute.ChooseDuration.serializer()
            )
            subclass(
                subclass = NavRoute.Appearance::class,
                serializer = NavRoute.Appearance.serializer()
            )
            subclass(
                subclass = NavRoute.FullScreen::class,
                serializer = NavRoute.FullScreen.serializer()
            )
            subclass(
                subclass = NavRoute.Account::class,
                serializer = NavRoute.Account.serializer()
            )
            subclass(
                subclass = NavRoute.Encrypt::class,
                serializer = NavRoute.Encrypt.serializer()
            )
            subclass(
                subclass = NavRoute.BiometricAuthentication::class,
                serializer = NavRoute.BiometricAuthentication.serializer()
            )
            subclass(
                subclass = NavRoute.NotificationSettings::class,
                serializer = NavRoute.NotificationSettings.serializer()
            )
            subclass(
                subclass = NavRoute.AppLanguage::class,
                serializer = NavRoute.AppLanguage.serializer()
            )
            subclass(
                subclass = NavRoute.TwelveWeekYear::class,
                serializer = NavRoute.TwelveWeekYear.serializer()
            )
            subclass(
                subclass = NavRoute.AddNewYear::class,
                serializer = NavRoute.AddNewYear.serializer()
            )
            subclass(
                subclass = NavRoute.YearDetail::class,
                serializer = NavRoute.YearDetail.serializer()
            )
            subclass(
                subclass = NavRoute.DatePicker::class,
                serializer = NavRoute.DatePicker.serializer()
            )
            subclass(
                subclass = NavRoute.WeekDetail::class,
                serializer = NavRoute.WeekDetail.serializer()
            )
            subclass(
                subclass = NavRoute.UpdateGoal::class,
                serializer = NavRoute.UpdateGoal.serializer()
            )
            subclass(
                subclass = NavRoute.AddGoal::class,
                serializer = NavRoute.AddGoal.serializer()
            )
        }
    }
}