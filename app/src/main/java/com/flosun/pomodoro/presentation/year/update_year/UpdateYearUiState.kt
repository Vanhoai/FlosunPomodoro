package com.flosun.pomodoro.presentation.year.update_year

data class UpdateYearUiState(
    val yearId: String? = null,

    val coverUri: String? = null,
    val name: String = "",
    val laggingIndicators: List<Pair<String, String>> = emptyList(), // Pair<Uuid, Name>
    val reward: String = "",
    val rewardImages: List<String> = emptyList(),
    val review: String = "",
    val stars: Int = 0,
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,

    val startTimeMilliseconds: Long = 0L,
    val endTimeMilliseconds: Long = 0L,
)