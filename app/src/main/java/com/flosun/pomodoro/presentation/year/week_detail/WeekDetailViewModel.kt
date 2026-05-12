package com.flosun.pomodoro.presentation.year.week_detail

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.core.functions.InternalStorageFuncs
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.core.services.LocationService
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.domain.repositories.CommonRepository
import com.flosun.pomodoro.domain.values.Location
import com.flosun.pomodoro.presentation.year.update_year.UpdateYearUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class WeekDetailUiEvent {

    data class ZoomAndShowMapClickEvent(
        val longitude: Double,
        val latitude: Double
    ) : WeekDetailUiEvent()

}

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
class WeekDetailViewModel @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
    private val locationService: LocationService,
    private val commonRepository: CommonRepository,
) : BaseViewModel(application) {

    private val _uiEvent = Channel<WeekDetailUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

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
            stars = weekEntity.stars,
            review = weekEntity.review,
            address = weekEntity.address,
            longitude = weekEntity.longitude,
            latitude = weekEntity.latitude,
        )

        _uiEvent.send(
            WeekDetailUiEvent.ZoomAndShowMapClickEvent(
                longitude = weekEntity.longitude,
                latitude = weekEntity.latitude,
            )
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

    fun onChangedStars(stars: Int) {
        _uiState.update { it.copy(stars = stars) }
    }

    fun onChangedReview(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun onChangedAddress(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    fun onChangedLngLat(longitude: Double, latitude: Double, isRunTrigger: Boolean = true) {
        _uiState.update { it.copy(longitude = longitude, latitude = latitude) }

        // Run trigger to find current address and save to ui state
        if (isRunTrigger) {
            ioRun {
                commonRepository.geocodingReverse(
                    longitude = longitude,
                    latitude = latitude,
                ).collect { result ->
                    when (result) {
                        is BaseResult.Loading -> {}
                        is BaseResult.Failure -> {
                            val msg = result
                                .exception
                                .message ?: "Failed to fetch address for the location."

                            showToast(msg)
                        }

                        is BaseResult.Success -> {
                            _uiState.update { it.copy(address = result.data ?: "") }
                        }
                    }
                }
            }
        }
    }

    fun fetchCurrentLocationAndUpdate(
        // callback to update camera position in map after fetching current location
        onSuccess: (longitude: Double, latitude: Double) -> Unit = { _, _ -> }
    ) = ioRun {
        try {
            val location = locationService.retrieveLastLocation()
            commonRepository.geocodingReverse(
                longitude = location.longitude,
                latitude = location.latitude,
            ).collect {
                when (it) {
                    is BaseResult.Loading -> {}
                    is BaseResult.Failure -> showToast(
                        it.exception.message ?: "Failed to fetch current location."
                    )

                    is BaseResult.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                longitude = location.longitude,
                                latitude = location.latitude,
                                address = it.data ?: "",
                            )
                        }

                        onSuccess(location.longitude, location.latitude)
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            showToast(exception.message ?: "Failed to fetch current location. Please try again.")
        }
    }

    fun resetState() {
        _uiState.value = WeekDetailUiState(
            reward = weekEntity.reward,
            rewardImages = weekEntity.rewardImages,
            stars = weekEntity.stars,
            review = weekEntity.review,
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
            stars = uiState.value.stars,
            review = uiState.value.review.trim(),
            // Location
            address = uiState.value.address.trim(),
            longitude = uiState.value.longitude,
            latitude = uiState.value.latitude,
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