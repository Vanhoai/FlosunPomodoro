package com.flosun.pomodoro.presentation.experiment

import android.app.Application
import androidx.compose.ui.geometry.Offset
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.presentation.swipe.Wave
import com.flosun.pomodoro.presentation.swipe.WaveShaderParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.Float
import kotlin.math.pow

data class WaveShader(
    val uGlobalDamping: Float,
    val uMinAmplitudeThreshold: Float,
    val uWaveOrigin: FloatArray,
    val uWaveAmplitude: Float,
    val uWaveFrequency: Float,
    val uWaveSpeed: Float,
    val uWaveStartTime: Float,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WaveShader

        if (uGlobalDamping != other.uGlobalDamping) return false
        if (uMinAmplitudeThreshold != other.uMinAmplitudeThreshold) return false
        if (uWaveAmplitude != other.uWaveAmplitude) return false
        if (uWaveFrequency != other.uWaveFrequency) return false
        if (uWaveSpeed != other.uWaveSpeed) return false
        if (uWaveStartTime != other.uWaveStartTime) return false
        if (!uWaveOrigin.contentEquals(other.uWaveOrigin)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uGlobalDamping.hashCode()
        result = 31 * result + uMinAmplitudeThreshold.hashCode()
        result = 31 * result + uWaveAmplitude.hashCode()
        result = 31 * result + uWaveFrequency.hashCode()
        result = 31 * result + uWaveSpeed.hashCode()
        result = 31 * result + uWaveStartTime.hashCode()
        result = 31 * result + uWaveOrigin.contentHashCode()
        return result
    }

}

@HiltViewModel
class ExperimentViewModel @Inject constructor(
    application: Application,
) : BaseViewModel(application) {

    private val damping = 0.55f
    private val minValueToRemoveWave = 0.3f
    private val minAmplitudeThresholdForShader = 2f
    private val initialAmplitude = 40f
    private val initialFrequency = 2f
    private val initialSpeed = 1000f

    private val _wave = MutableStateFlow<Wave?>(null)
    val wave: StateFlow<Wave?> = _wave.asStateFlow()

    fun cleanupWaves(currentTimeSeconds: Float) {}

    fun addWave(position: Offset, time: Float) {
        val newWave = Wave(
            origin = position,
            startTime = time,
            amplitude = initialAmplitude,
            frequency = initialFrequency,
            speed = initialSpeed
        )

        Timber.tag(DEBUG_TAG).d("Adding wave at position: $position, time: $time")
    }

    fun buildShaderUniforms(time: Float): WaveShader {
        val origin = FloatArray(2)
        origin[0] = wave.value?.origin?.x ?: 0f
        origin[1] = wave.value?.origin?.y ?: 0f

        return WaveShader(
            uGlobalDamping = damping,
            uMinAmplitudeThreshold = minAmplitudeThresholdForShader,
            uWaveOrigin = origin,
            uWaveAmplitude = wave.value?.amplitude ?: 0f,
            uWaveFrequency = wave.value?.frequency ?: 0f,
            uWaveSpeed = wave.value?.speed ?: 0f,
            uWaveStartTime = wave.value?.startTime ?: 0f
        )
    }
}