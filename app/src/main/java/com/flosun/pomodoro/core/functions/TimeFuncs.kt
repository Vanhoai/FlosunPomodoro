package com.flosun.pomodoro.core.functions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.Instant

object TimeFuncs {

    fun convertToMilliseconds(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
    ): Long {
        val localDateTime = LocalDateTime(
            year,
            month,
            day,
            hour = hour,
            minute = minute,
            second = second,
        )

        val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())
        return instant.toEpochMilliseconds()
    }

    fun dateTimeFromMilliseconds(milliseconds: Long): LocalDateTime {
        return Instant
            .fromEpochMilliseconds(milliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun formatTimeString(milliseconds: Long): String {
        val localDateTime = Instant
            .fromEpochMilliseconds(milliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val year = localDateTime.year
        val month = localDateTime.month.number
        val day = localDateTime.day

        // Make sure month and day are always two digits
        val monthString = if (month < 10) "0$month" else "$month"
        val dayString = if (day < 10) "0$day" else "$day"
        return "$year/$monthString/$dayString"
    }

    fun isTimeOverlap(
        startTime1: Long,
        endTime1: Long,
        startTime2: Long,
        endTime2: Long,
    ): Boolean {
        return startTime1 < endTime2 && startTime2 < endTime1
    }

    fun formatDuration(startTimeMilliseconds: Long, endTimeMilliseconds: Long): String {
        return "${formatTimeString(startTimeMilliseconds)} - ${formatTimeString(endTimeMilliseconds)}"
    }

    fun nowMilliseconds(): Long {
        return Clock.System.todayIn(TimeZone.currentSystemDefault())
            .atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    fun formatTimeAMPM(millis: Int): String {
        val hours = millis / 3600
        val minutes = (millis % 3600) / 60

        val amPm = if (hours < 12) "AM" else "PM"
        val displayHours = if (hours % 12 == 0) 12 else hours % 12

        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHours, minutes, amPm)
    }


    // This function assumes the input time format is "HH:mm" (24-hour format)
    fun parseTimeToMs(time: String): Int {
        val parts = time.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Time must be in the format HH:mm")
        }

        val hours = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid hours")
        val minutes = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid minutes")

        if (hours !in 0..23) {
            throw IllegalArgumentException("Hours must be between 0 and 23")
        }
        if (minutes !in 0..59) {
            throw IllegalArgumentException("Minutes must be between 0 and 59")
        }

        return hours * 3600 + minutes * 60
    }
}