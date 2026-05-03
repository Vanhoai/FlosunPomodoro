package com.flosun.pomodoro.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
)