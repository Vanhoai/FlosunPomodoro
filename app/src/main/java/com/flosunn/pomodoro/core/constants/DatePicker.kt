package com.flosunn.pomodoro.core.constants

import android.icu.util.Calendar

internal object DatePickerCommon {
    private const val repeatCount = 100
    private val monthNames = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    val years = List(200) { it + Calendar.getInstance()[Calendar.YEAR] - 100 }
    
    fun getMonths(): List<String> {
        val repeatedMonths = mutableListOf<String>()
        repeat(repeatCount) {
            repeatedMonths.addAll(monthNames)
        }

        return repeatedMonths
    }
}