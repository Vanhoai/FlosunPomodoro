package com.flosun.pomodoro.presentation.year.week_detail

data class WeekDetailUiState(
    val name: String = "",
    val progress: Int = 0,
    val duration: String = "",
    // Reward
    val reward: String = "",
    val rewardImages: List<String> = emptyList(),
    // Review
    val stars: Int = 0,
    val review: String = "",
    // Location
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
)