package com.flosun.pomodoro.presentation.swipe.settings.biometric_authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.core.constants.IS_ENABLED_BIOMETRIC_KEY
import com.flosun.pomodoro.core.constants.IS_ENABLED_FACE_RECOGNITION_KEY
import com.flosun.pomodoro.core.utils.rememberPreference
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.RowSwitch
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun BiometricAuthenticationView(
    navBackStack: NavBackStack<NavKey>,
    biometricAuthenticationViewModel: BiometricAuthenticationViewModel = hiltViewModel<BiometricAuthenticationViewModel>()
) {
    var isEnabledBiometric by rememberPreference(IS_ENABLED_BIOMETRIC_KEY, false)
    var isEnabledFaceRecognition by rememberPreference(IS_ENABLED_FACE_RECOGNITION_KEY, false)

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
                title = "Biometric Authentication"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White),
            ) {
                RowSwitch(
                    title = "Biometric Authentication",
                    isActive = isEnabledBiometric,
                    onChange = { isEnabledBiometric = it }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowSwitch(
                    title = "Face Recognition",
                    isActive = isEnabledFaceRecognition,
                    onChange = { isEnabledFaceRecognition = it }
                )
            }
        }
    }
}