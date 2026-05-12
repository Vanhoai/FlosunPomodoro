package com.flosun.pomodoro.presentation.swipe.settings.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.CURRENT_THEME_KEY
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosun.pomodoro.ui.theme.ThemeMode
import com.flosunn.core.libraries.datastore.rememberEnumPreference
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceView(navBackStack: NavBackStack<NavKey>) {
    val scope = rememberCoroutineScope()
    var currentTheme by rememberEnumPreference(CURRENT_THEME_KEY, ThemeMode.SYSTEM)

    val themeSheetState = rememberModalBottomSheetState()
    var isShowThemeSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppTheme.colors.backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(
                onBack = { navBackStack.removeLastOrNull() },
                title = "App Appearance"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White),
            ) {
                RowNavigation(
                    title = "Theme",
                    description = "Light",
                    onPress = { isShowThemeSheet = true }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowNavigation(
                    title = "App Language",
                    description = "English",
                    onPress = { navBackStack.add(NavRoute.AppLanguage) }
                )
            }
        }

        if (isShowThemeSheet) ThemeBottomSheet(
            sheetState = themeSheetState,
            closeBottomSheet = {
                scope.launch { themeSheetState.hide() }.invokeOnCompletion {
                    if (!themeSheetState.isVisible) {
                        isShowThemeSheet = false
                    }
                }
            }
        )
    }
}

val themes = listOf(
    ThemeMode.SYSTEM,
    ThemeMode.LIGHT,
    ThemeMode.DARK,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeBottomSheet(
    sheetState: SheetState,
    closeBottomSheet: () -> Unit = {},
) {
    var selectedOption by remember { mutableIntStateOf(0) }

    CoreBottomSheet(
        sheetState = sheetState,
        title = "Sound",
        closeBottomSheet = closeBottomSheet,
    ) {
        themes.forEachIndexed { index, theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = { selectedOption = index },
                    )
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(
                        when (theme) {
                            ThemeMode.SYSTEM -> R.drawable.ic_link
                            ThemeMode.LIGHT -> R.drawable.ic_sun
                            ThemeMode.DARK -> R.drawable.ic_moon
                        }
                    ),
                    contentDescription = null,
                    tint = if (selectedOption == index) AppTheme.colors.primaryColor else Color(
                        0xFF757575
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = when (theme) {
                        ThemeMode.SYSTEM -> "System"
                        ThemeMode.LIGHT -> "Light"
                        ThemeMode.DARK -> "Dark"
                    },
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )

                if (selectedOption == index) Icon(
                    painter = painterResource(R.drawable.ic_checked),
                    contentDescription = null,
                    tint = AppTheme.colors.primaryColor,
                )
            }
        }

        CoreButton(
            modifier = Modifier.padding(20.dp),
            onPress = { closeBottomSheet() }
        ) {
            Text(
                text = "Apply",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}