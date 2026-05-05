package com.flosun.pomodoro.presentation.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.flosun.pomodoro.presentation.account.AccountView
import com.flosun.pomodoro.presentation.auth.AuthView
import com.flosun.pomodoro.presentation.datepicker.DatePickerView
import com.flosun.pomodoro.presentation.encrypt.EncryptView
import com.flosun.pomodoro.presentation.goals.add_goal.AddGoalView
import com.flosun.pomodoro.presentation.goals.update_goal.UpdateGoalView
import com.flosun.pomodoro.presentation.swipe.SwipeView
import com.flosun.pomodoro.presentation.swipe.home.fullscreen.FullScreenView
import com.flosun.pomodoro.presentation.week_year.add_new_year.AddNewYearView
import com.flosun.pomodoro.presentation.swipe.settings.appearance.AppearanceView
import com.flosun.pomodoro.presentation.swipe.settings.biometric_authentication.BiometricAuthenticationView
import com.flosun.pomodoro.presentation.swipe.settings.language.AppLanguageView
import com.flosun.pomodoro.presentation.swipe.settings.notification.NotificationSettingsView
import com.flosun.pomodoro.presentation.swipe.settings.preference.PreferenceView
import com.flosun.pomodoro.presentation.swipe.settings.preference.choose_duration.ChooseDurationView
import com.flosun.pomodoro.presentation.week_year.twelve_week_year.TwelveWeekYearView
import com.flosun.pomodoro.presentation.week_year.update_year.UpdateYearView
import com.flosun.pomodoro.presentation.week_year.week_detail.WeekDetailView
import com.flosun.pomodoro.presentation.week_year.year_detail.YearDetailView
import com.flosun.pomodoro.ui.components.shared.NotFoundView
import com.flosun.pomodoro.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navBackStack: NavBackStack<NavKey>,
) {
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
                is NavRoute.UpdateYear -> NavEntry(key) { UpdateYearView(key) }
                is NavRoute.YearDetail -> NavEntry(key) { YearDetailView(navBackStack, key) }
                is NavRoute.DatePicker -> NavEntry(key) { DatePickerView(navBackStack) }
                is NavRoute.WeekDetail -> NavEntry(key) { WeekDetailView(navBackStack, key) }
                is NavRoute.UpdateGoal -> NavEntry(key) { UpdateGoalView() }
                is NavRoute.AddGoal -> NavEntry(key) { AddGoalView() }
                else -> NavEntry(key) { NotFoundView() }
            }
        }
    )
}