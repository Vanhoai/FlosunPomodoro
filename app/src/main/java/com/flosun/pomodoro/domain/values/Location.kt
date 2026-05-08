package com.flosun.pomodoro.domain.values


data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
)