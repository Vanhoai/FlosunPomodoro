package com.flosunn.pomodoro.presentation.auth

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.adapters.database.LocalDatabase
import com.flosunn.pomodoro.presentation.auth.components.AuthForm
import com.flosunn.pomodoro.presentation.auth.components.BiometricSignInForm
import com.flosunn.pomodoro.presentation.auth.components.SocialButtons
import com.flosunn.pomodoro.presentation.graph.NavGraph
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.shared.rememberGlobalLoading
import com.flosunn.pomodoro.ui.theme.AppTheme
import com.flosunn.pomodoro.ui.theme.PomodoroTheme
import kotlinx.coroutines.delay
import timber.log.Timber

@SuppressLint("ContextCastToActivity")
@Composable
fun AuthView(
    navBackStack: NavBackStack<NavKey>,
    authViewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
) {
    val context = LocalContext.current as FragmentActivity
    val globalLoading = rememberGlobalLoading()

    LaunchedEffect(Unit) {
        authViewModel.navigationEvent.collect { event ->
            when (event) {
                AuthNavigationEvent.NavigateToMainScreen -> navBackStack.add(NavRoute.Swipe)
                AuthNavigationEvent.NavigateToFaceRecognition -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        globalLoading.setLoading(
            isLoading = true,
            message = "Checking Authentication"
        )

        val isAuthenticated = authViewModel.checkAuthentication()
        delay(1000)
        globalLoading.setLoading(false)

        delay(500)
        if (isAuthenticated) navBackStack.add(NavRoute.Swipe)
    }

    Scaffold(
        containerColor = AppTheme.colors.backgroundColor,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .size(90.dp),
            )

            Text(
                text = "Welcome to T&P, also known as the 12-Week Year Method and the Pomodoro Method. This is definitely the place where you can perfectly manage all your goals and plans.",
                fontSize = 14.sp,
                color = Color(0xFF797979),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .padding(horizontal = 20.dp)
            )

            Text(
                text = "Sign In",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 40.dp),
            )

            SocialButtons(
                signInGoogle = {
                    authViewModel.signInWithGoogle()
                },
                signInGithub = {},
            )

            BiometricSignInForm(
                authWithBiometric = {
                    authViewModel.authWithBiometric(context)
                },
                authWithFaceRecognition = {},
            )
        }
    }
}
