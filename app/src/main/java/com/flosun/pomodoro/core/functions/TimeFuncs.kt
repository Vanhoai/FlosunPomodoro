package com.flosun.pomodoro.core.functions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
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
}