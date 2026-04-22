package com.flosunn.pomodoro.presentation.graph

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.flosunn.pomodoro.presentation.account.AccountView
import com.flosunn.pomodoro.presentation.auth.AuthView
import com.flosunn.pomodoro.presentation.datepicker.DatePickerView
import com.flosunn.pomodoro.presentation.encrypt.EncryptView
import com.flosunn.pomodoro.presentation.swipe.SwipeView
import com.flosunn.pomodoro.presentation.swipe.home.fullscreen.FullScreenView
import com.flosunn.pomodoro.presentation.swipe.settings.add_new_year.AddNewYearView
import com.flosunn.pomodoro.presentation.swipe.settings.appearance.AppearanceView
import com.flosunn.pomodoro.presentation.swipe.settings.biometric_authentication.BiometricAuthenticationView
import com.flosunn.pomodoro.presentation.swipe.settings.language.AppLanguageView
import com.flosunn.pomodoro.presentation.swipe.settings.notification.NotificationSettingsView
import com.flosunn.pomodoro.presentation.swipe.settings.preference.PreferenceView
import com.flosunn.pomodoro.presentation.swipe.settings.preference.choose_duration.ChooseDurationView
import com.flosunn.pomodoro.presentation.swipe.settings.twelve_week_year.TwelveWeekYearView
import com.flosunn.pomodoro.presentation.swipe.settings.year_detail.YearDetailView
import com.flosunn.pomodoro.ui.components.shared.NotFoundView
import com.flosunn.pomodoro.ui.theme.AppTheme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navBackStack = rememberNavBackStack(
        configuration = config,
        NavRoute.DatePicker
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
                is NavRoute.Preference -> NavEntry(key) { PreferenceView(navBackStack) }
                is NavRoute.ChooseDuration -> NavEntry(key) {
                    ChooseDurationView(
                        navBackStack,
                        key
                    )
                }

                is NavRoute.Appearance -> NavEntry(key) { AppearanceView(navBackStack) }
                is NavRoute.FullScreen -> NavEntry(key) { FullScreenView(navBackStack) }
                is NavRoute.Account -> NavEntry(key) { AccountView(navBackStack) }
                is NavRoute.Encrypt -> NavEntry(key) { EncryptView() }
                is NavRoute.BiometricAuthentication -> NavEntry(key) {
                    BiometricAuthenticationView(
                        navBackStack
                    )
                }

                is NavRoute.NotificationSettings -> NavEntry(key) {
                    NotificationSettingsView(
                        navBackStack
                    )
                }

                is NavRoute.AppLanguage -> NavEntry(key) { AppLanguageView(navBackStack) }
                is NavRoute.TwelveWeekYear -> NavEntry(key) { TwelveWeekYearView(navBackStack) }
                is NavRoute.AddNewYear -> NavEntry(key) { AddNewYearView(navBackStack) }
                is NavRoute.YearDetail -> NavEntry(key) { YearDetailView(navBackStack) }
                is NavRoute.DatePicker -> NavEntry(key) { DatePickerView(navBackStack) }
                else -> NavEntry(key) { NotFoundView() }
            }
        }
    )
}