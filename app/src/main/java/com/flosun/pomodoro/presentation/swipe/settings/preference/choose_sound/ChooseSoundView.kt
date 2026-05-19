package com.flosun.pomodoro.presentation.swipe.settings.preference.choose_sound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.adapters.database.entities.SoundType
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.TwoOptionActions
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datastore.rememberPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class ChooseSoundType {
    FOCUS_TICKING,
    SHORT_BREAK_TICKING,
    LONG_BREAK_TICKING,
    FOCUS_ALARM,
    SHORT_BREAK_ALARM,
    LONG_BREAK_ALARM;

    fun toTitle(): String {
        return when (this) {
            FOCUS_TICKING -> "Focus Ticking"
            SHORT_BREAK_TICKING -> "Short Break Ticking"
            LONG_BREAK_TICKING -> "Long Break Ticking"
            FOCUS_ALARM -> "Focus Alarm"
            SHORT_BREAK_ALARM -> "Short Break Alarm"
            LONG_BREAK_ALARM -> "Long Break Alarm"
        }
    }
}

@Composable
fun ChooseSoundView(navRoute: NavRoute.ChooseSound) {
    val database = LocalDatabase.current
    val context = LocalContext.current
    val navBackStack = LocalNavBackStack.current

    val accountId by rememberPreference(CURRENT_ACCOUNT_ID_KEY, "")
    val setting by database.findSettingByAccountId(accountId).collectAsState(null)
    if (setting == null) return

    val coroutineScope = rememberCoroutineScope()
    val player = ExoPlayer.Builder(context).build()
    DisposableEffect(Unit) {
        onDispose {
            player.release()
            coroutineScope.cancel()
        }
    }

    var selectedSound by remember { mutableStateOf<SoundEntity?>(null) }
    var sounds by remember { mutableStateOf<List<SoundEntity>>(emptyList()) }

    LaunchedEffect(Unit) {
        when (navRoute.type) {
            ChooseSoundType.FOCUS_TICKING,
            ChooseSoundType.SHORT_BREAK_TICKING,
            ChooseSoundType.LONG_BREAK_TICKING -> database
                .findSoundsByType(SoundType.TICKING)
                .collect { sounds = it }

            ChooseSoundType.FOCUS_ALARM,
            ChooseSoundType.SHORT_BREAK_ALARM,
            ChooseSoundType.LONG_BREAK_ALARM -> database
                .findSoundsByType(SoundType.ALARM)
                .collect { sounds = it }
        }
    }

    LaunchedEffect(navRoute.type, sounds) {
        selectedSound = when (navRoute.type) {
            ChooseSoundType.FOCUS_TICKING -> sounds.firstOrNull { it.id == setting!!.focusTicking }
            ChooseSoundType.SHORT_BREAK_TICKING -> sounds.firstOrNull { it.id == setting!!.shortBreakTicking }
            ChooseSoundType.LONG_BREAK_TICKING -> sounds.firstOrNull { it.id == setting!!.longBreakTicking }
            ChooseSoundType.FOCUS_ALARM -> sounds.firstOrNull { it.id == setting!!.focusAlarm }
            ChooseSoundType.SHORT_BREAK_ALARM -> sounds.firstOrNull { it.id == setting!!.shortBreakAlarm }
            ChooseSoundType.LONG_BREAK_ALARM -> sounds.firstOrNull { it.id == setting!!.longBreakAlarm }
        }
    }

    fun updateSound() {
        if (selectedSound == null) return
        coroutineScope.launch(Dispatchers.IO) {
            val updatedSetting = when (navRoute.type) {
                ChooseSoundType.FOCUS_TICKING -> setting!!.copy(focusTicking = selectedSound!!.id)
                ChooseSoundType.SHORT_BREAK_TICKING -> setting!!.copy(shortBreakTicking = selectedSound!!.id)
                ChooseSoundType.LONG_BREAK_TICKING -> setting!!.copy(longBreakTicking = selectedSound!!.id)
                ChooseSoundType.FOCUS_ALARM -> setting!!.copy(focusAlarm = selectedSound!!.id)
                ChooseSoundType.SHORT_BREAK_ALARM -> setting!!.copy(shortBreakAlarm = selectedSound!!.id)
                ChooseSoundType.LONG_BREAK_ALARM -> setting!!.copy(longBreakAlarm = selectedSound!!.id)
            }

            database.updateSetting(updatedSetting)
            delay(1000L)
            navBackStack.removeLastOrNull()
        }
    }

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(title = navRoute.type.toTitle())
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White)
            ) {
                sounds.forEachIndexed { index, sound ->
                    val isSelected = selectedSound?.id == sound.id
                    val color = if (isSelected) AppTheme.colors.primaryColor else Color(0xFF757575)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .rippleEffectClickable {
                                selectedSound = sound
                                coroutineScope.launch {
                                    player.setMediaItem(sound.toMediaItem())
                                    player.prepare()
                                    player.play()
                                }
                            }
                            .height(52.dp)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sound),
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.padding(end = 12.dp),
                        )

                        Text(
                            text = sound.title,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Start,
                            color = color,
                        )

                        if (isSelected) Icon(
                            painter = painterResource(R.drawable.ic_checked),
                            contentDescription = null,
                            tint = Color(0xFF3FA039),
                        )
                    }

                    if (index != sounds.lastIndex) HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE0E0E0)
                    )
                }
            }

            CoreButton(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Custom Sound",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            VerticalDivider(modifier = Modifier.weight(1f))
            TwoOptionActions(
                modifier = Modifier.padding(20.dp),
                okLabel = "Save",
                onOk = ::updateSound,
            )
        }
    }
}