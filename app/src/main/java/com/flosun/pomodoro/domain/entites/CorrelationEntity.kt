package com.flosun.pomodoro.domain.entites

import org.flosun.ksp_processing.annotations.GenerateToMap
import org.flosun.ksp_processing.annotations.GenerateUpdate

@GenerateToMap
@GenerateUpdate
data class CorrelationEntity(
    val id: String,
    val context: String,
    val requestId: String,
    val requestTime: Long,
)