package com.flosunn.pomodoro.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun AuthView(navBackStack: NavBackStack<NavKey>) {
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
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(90.dp),
            )

            Text(
                text = "Welcome to T&P, also known as the 12-Week Year Method and the Pomodoro Method. This is definitely the place where you can perfectly manage all your goals and plans.",
                fontSize = 14.sp,
                color = Color(0xFF797979),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .padding(horizontal = 40.dp)
            )

            Text(
                text = "Sign In to Continue",
                fontSize = 18.sp,
            )

            SocialButtons()

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                color = Color(0xFFD5D5D5),
            )

            AuthForm(navigateToSwipe = { navBackStack.add(NavRoute.Swipe) })
        }
    }
}