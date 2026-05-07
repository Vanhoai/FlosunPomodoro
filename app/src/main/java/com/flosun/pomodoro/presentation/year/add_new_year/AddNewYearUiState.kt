package com.flosun.pomodoro.presentation.year.add_new_year


data class AddNewYearUiState(
    val coverUri: String? = null,
    val name: String = "",
    val startTimeMilliseconds: Long = 0L,
    val endTimeMilliseconds: Long = 0L,
    val laggingIndicators: List<Pair<String, String>> = emptyList(), // Pair<Uuid, Name>
    val reward: String = "",
    val rewardImages: List<String> = emptyList(),
    val longitude: Double? = null,
    val latitude: Double? = null,
    val address: String = "",
)
