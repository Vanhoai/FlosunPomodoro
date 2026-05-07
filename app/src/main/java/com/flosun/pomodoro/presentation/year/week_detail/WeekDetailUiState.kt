package com.flosun.pomodoro.presentation.year.week_detail

data class WeekDetailUiState(
    val name: String = "",
    val progress: Int = 0,
    val duration: String = "",
    val reward: String = "",
    val rewardImages: List<String> = emptyList(),
)