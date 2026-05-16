package com.flosun.pomodoro.presentation.swipe

import android.app.Application
import androidx.compose.ui.geometry.Offset
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.pow

data class Wave(
    val origin: Offset,
    val startTime: Float,
    val amplitude: Float,
    val frequency: Float,
    val speed: Float
)

data class WaveShaderParams(
    val numWaves: Int,
    val origins: FloatArray,
    val amplitudes: FloatArray,
    val frequencies: FloatArray,
    val speeds: FloatArray,
    val startTimes: FloatArray,
    val globalDamping: Float,
    val minAmplitudeThreshold: Float
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WaveShaderParams

        if (numWaves != other.numWaves) return false
        if (globalDamping != other.globalDamping) return false
        if (minAmplitudeThreshold != other.minAmplitudeThreshold) return false
        if (!origins.contentEquals(other.origins)) return false
        if (!amplitudes.contentEquals(other.amplitudes)) return false
        if (!frequencies.contentEquals(other.frequencies)) return false
        if (!speeds.contentEquals(other.speeds)) return false
        if (!startTimes.contentEquals(other.startTimes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numWaves
        result = 31 * result + globalDamping.hashCode()
        result = 31 * result + minAmplitudeThreshold.hashCode()
        result = 31 * result + origins.contentHashCode()
        result = 31 * result + amplitudes.contentHashCode()
        result = 31 * result + frequencies.contentHashCode()
        result = 31 * result + speeds.contentHashCode()
        result = 31 * result + startTimes.contentHashCode()
        return result
    }
}

@HiltViewModel
class SwipeViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    // Damping factor controlling amplitude decay speed.
    private val damping = 0.55f
    private val minValueToRemoveWave = 0.3f
    private val minAmplitudeThresholdForShader = 2f
    private val lastEmissionMap = mutableMapOf<Int, Float>()
    private val emissionCooldownSeconds = 0.05f
    private val initialAmplitude = 30f
    private val initialFrequency = 2f
    private val initialSpeed = 1000f
    private val lastPositions = mutableMapOf<Int, Offset>()

    private val _waves = MutableStateFlow<List<Wave>>(emptyList())
    val waves: StateFlow<List<Wave>> = _waves

    fun cleanupWaves(currentTimeSeconds: Float) {
        _waves.value = _waves.value.filter { wave ->
            val elapsedSeconds = currentTimeSeconds - wave.startTime
            val currentAmplitude = wave.amplitude * damping.pow(elapsedSeconds)
            currentAmplitude >= minValueToRemoveWave
        }
    }

    fun addWave(position: Offset, pointerId: Int, time: Float) {
        val lastEmission = lastEmissionMap[pointerId] ?: 0f
        val lastPos = lastPositions[pointerId]
        val minDistance = 15f

        if (time - lastEmission >= emissionCooldownSeconds &&
            (lastPos == null || (position - lastPos).getDistance() > minDistance)
        ) {
            val newWave = Wave(
                origin = position,
                startTime = time,
                amplitude = initialAmplitude,
                frequency = initialFrequency,
                speed = initialSpeed
            )

            val currentWaves = _waves.value.toMutableList()

            if (currentWaves.size >= 20) {
                val weakestWave = currentWaves.minByOrNull { wave ->
                    val elapsedSeconds = time - wave.startTime
                    wave.amplitude * damping.pow(elapsedSeconds)
                }
                if (weakestWave != null) {
                    currentWaves.remove(weakestWave)
                }
            }

            currentWaves.add(newWave)
            _waves.value = currentWaves

            lastEmissionMap[pointerId] = time
            lastPositions[pointerId] = position
        }
    }

    fun buildShaderUniforms(time: Float): WaveShaderParams {
        val activeWaves = _waves.value
        val maxWaves = 20

        val origins = FloatArray(maxWaves * 2)
        val amplitudes = FloatArray(maxWaves)
        val frequencies = FloatArray(maxWaves)
        val speeds = FloatArray(maxWaves)
        val startTimes = FloatArray(maxWaves)

        activeWaves.take(maxWaves).forEachIndexed { index, wave ->
            val elapsedSeconds = time - wave.startTime
            val currentAmplitude = wave.amplitude * damping.pow(elapsedSeconds)

            origins[index * 2] = wave.origin.x
            origins[index * 2 + 1] = wave.origin.y
            amplitudes[index] = currentAmplitude
            frequencies[index] = wave.frequency
            speeds[index] = wave.speed
            startTimes[index] = wave.startTime
        }

        return WaveShaderParams(
            numWaves = activeWaves.size.coerceAtMost(maxWaves),
            origins = origins,
            amplitudes = amplitudes,
            frequencies = frequencies,
            speeds = speeds,
            startTimes = startTimes,
            globalDamping = damping,
            minAmplitudeThreshold = minAmplitudeThresholdForShader
        )
    }
}