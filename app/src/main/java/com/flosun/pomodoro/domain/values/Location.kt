package com.flosun.pomodoro.domain.values

import kotlinx.serialization.Serializable


@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
)