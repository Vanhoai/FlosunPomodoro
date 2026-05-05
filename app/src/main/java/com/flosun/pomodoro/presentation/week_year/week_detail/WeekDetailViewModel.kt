package com.flosun.pomodoro.presentation.week_year.week_detail

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.core.functions.InternalStorageFuncs
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
class WeekDetailViewModel @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
) : BaseViewModel(application) {

    private lateinit var weekEntity: WeekEntity
    private lateinit var weekToUpdate: WeekEntity

    private val _uiState = MutableStateFlow(WeekDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun initialize(weekId: String) = ioRun {
        weekEntity = database.findWeekById(weekId).first() ?: return@ioRun

        val duration = TimeFuncs.formatDuration(
            weekEntity.startTimeMilliseconds,
            weekEntity.endTimeMilliseconds
        )

        _uiState.value = WeekDetailUiState(
            duration = duration,
            reward = weekEntity.reward,
            rewardImages = weekEntity.rewardImages,
        )
    }

    fun updateReward(reward: String) {
        _uiState.value = _uiState.value.copy(reward = reward)
    }

    fun updateRewardImages(newImages: List<String>) {
        _uiState.update { it.copy(rewardImages = newImages) }
    }

    fun deleteRewardImage(imageUri: String) {
        _uiState.update { state ->
            val newList = state.rewardImages.filterNot { it == imageUri }
            state.copy(rewardImages = newList)
        }
    }

    fun resetState() {
        _uiState.value = WeekDetailUiState(
            reward = weekEntity.reward,
            rewardImages = weekEntity.rewardImages,
        )
    }

    fun updateWeek(onSuccess: () -> Unit = {}) = ioRun {
        if (!validateReward()) return@ioRun

        val folder = getCurrentYearDirectory(yearId = weekEntity.yearId)
        if (folder == null) {
            showToast("Failed to access storage. Please try again.")
            return@ioRun
        }

        val newRewardImages = updateRewardImages(folder)
        weekToUpdate = weekEntity.copy(
            reward = uiState.value.reward.trim(),
            rewardImages = newRewardImages,
        )

        val updatedResult = database.updateWeek(weekToUpdate)
        if (updatedResult < 1) {
            showToast("Failed to update week. Please try again.")
            return@ioRun
        }

        showToast("Week updated successfully.")
        // Update the original weekEntity with the new values after successful update
        weekEntity = weekToUpdate

        delay(500L)
        onSuccess()
    }


    // =================================== PRIVATE FUNCTIONS ===================================

    suspend fun updateRewardImages(folder: String): List<String> {
        val newRewardImages = uiState.value.rewardImages
        val oldRewardImages = weekEntity.rewardImages

        if (newRewardImages.toSet() == oldRewardImages.toSet()) return weekEntity.rewardImages

        val imagesToDelete = oldRewardImages.toSet().subtract(newRewardImages.toSet())
        val imagesToCopy = newRewardImages.toSet().subtract(oldRewardImages.toSet())

        imagesToDelete.forEach { imageUri ->
            val result = InternalStorageFuncs.deleteFileFromInternalStorage(imageUri)
            if (!result) showToast("Failed to delete old reward images. Please try again.")
        }

        val copiedImages = imagesToCopy.map { imageUrl ->
            val imagePath = "$folder/REWARD-${Uuid.random()}.jpg"
            val newUri = InternalStorageFuncs.copyImageToInternalStorageApp(
                context = application.applicationContext,
                uri = imageUrl,
                path = imagePath,
            )
            if (newUri.isBlank()) {
                showToast("Failed to save new reward images. Please try again.")
            }
            newUri
        }.filter { it.isNotBlank() }

        return oldRewardImages
            .filterNot { it in imagesToDelete }
            .plus(copiedImages)
    }

    suspend fun validateReward(): Boolean {
        val reward = uiState.value.reward.trim()
        if (reward.isEmpty()) {
            showToast("Reward cannot be empty.")
            return false
        }
        return true
    }
}