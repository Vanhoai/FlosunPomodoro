package com.flosun.pomodoro.presentation.week_year.add_new_year


data class AddNewYearUIState(
    val coverUri: String? = null,
    val name: String = "",
    val startTimeMilliseconds: Long = 0L,
    val endTimeMilliseconds: Long = 0L,
    val laggingIndicators: List<Pair<String, String>> = emptyList(), // Pair<Uuid, Name>
    val reward: String = "",
    val rewardImages: List<String> = emptyList(),
)
