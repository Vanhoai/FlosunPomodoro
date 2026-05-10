package com.flosun.pomodoro.presentation.swipe.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.rememberMessageManager
import com.flosun.pomodoro.ui.components.shared.LocalGlobalLoading
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun SignOutButton() {
    val navBackStack = LocalNavBackStack.current
    val globalLoading = LocalGlobalLoading.current

    val messageManager = rememberMessageManager()
    val scope = rememberCoroutineScope()

    fun signOut() {
//        scope.launch {
//            globalLoading.setLoading(true, "Signing Out")
//            Firebase.auth.signOut()
//            navBackStack.clear()
//            globalLoading.setLoading(false)
//            navBackStack.add(NavRoute.Auth)
//        }

        messageManager.addMessage(
            title = "Feature Unavailable",
            description = "Sign out functionality is currently unavailable. Please try again later."
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(52.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color(0xFFECECEC),
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .rippleEffectClickable { signOut() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logout),
            contentDescription = null,
            tint = Color(0xFFFF6767),
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = "Logout",
            fontSize = 16.sp,
            color = Color(0xFFFF6767),
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}