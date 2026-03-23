package com.flosunn.pomodoro.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.unit.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CalendarWidget : GlanceAppWidget() {
    @SuppressLint("RestrictedApi")
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime = timeFormat.format(Date())

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(12.dp)
                    .background(Color(0xFF404040).copy(alpha = 0.6f)),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                Text(
                    text = currentTime,
                    style = TextStyle(
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Normal,
                        fontSize = 48.sp,
                        color = ColorProvider(Color.White)
                    )
                )
            }
        }
    }
}


class CalendarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CalendarWidget
}
