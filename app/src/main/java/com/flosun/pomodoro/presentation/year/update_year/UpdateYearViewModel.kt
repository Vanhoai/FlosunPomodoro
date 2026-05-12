package com.flosun.pomodoro.presentation.year.update_year

import android.app.Application
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.functions.InternalStorageFuncs
import com.flosun.pomodoro.core.services.LocationService
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.BaseResult
import com.flosun.pomodoro.core.utils.BaseViewModel
import com.flosun.pomodoro.domain.repositories.CommonRepository
import com.flosun.pomodoro.presentation.auth.AuthNavigationEvent
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosunn.core.CoreModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class UpdateYearUiEvent {

    data class ZoomAndShowMapClickEvent(
        val longitude: Double,
        val latitude: Double
    ) : UpdateYearUiEvent()

}

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel
class UpdateYearViewModel @Inject constructor(
    private val application: Application,
    private val database: PomodoroDatabase,
    private val appStorage: AppStorage,
    private val globalLoading: GlobalLoading,
    private val commonRepository: CommonRepository,
    private val locationService: LocationService,
) : BaseViewModel(application) {

    private val _uiEvent = Channel<UpdateYearUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    // For storing initial values to compare with updated values later when saving
    lateinit var yearEntity: TwelveWeekYearEntity
    lateinit var yearToUpdate: TwelveWeekYearEntity
    lateinit var initialLaggingIndicators: List<Pair<String, String>>

    private val _uiState = MutableStateFlow(UpdateYearUiState())
    val uiState = _uiState.asStateFlow()

    fun initialize(yearId: String) = ioRun {
        val year = database.findTwelveWeekYearById(yearId).first() ?: return@ioRun
        val laggingIndicators = database.findLaggingIndicatorsByYearId(yearId).first()

        // Update states
        _uiState.update {
            it.copy(
                yearId = yearId,
                coverUri = year.cover,
                name = year.name,
                startTimeMilliseconds = year.startTimeMilliseconds,
                endTimeMilliseconds = year.endTimeMilliseconds,
                laggingIndicators = laggingIndicators.map { indicator ->
                    Pair(
                        indicator.id,
                        indicator.name,
                    )
                },
                reward = year.reward,
                rewardImages = year.rewardImages,
                review = year.review,
                stars = year.stars,
                address = year.address,
                longitude = year.longitude,
                latitude = year.latitude,
            )
        }

        // Store initial values for later comparison when saving
        yearEntity = year
        yearToUpdate = year
        initialLaggingIndicators = laggingIndicators.map { Pair(it.id, it.name) }

        _uiEvent.send(UpdateYearUiEvent.ZoomAndShowMapClickEvent(year.longitude, year.latitude))
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateCoverUri(uri: String) {
        _uiState.update { it.copy(coverUri = uri) }
    }

    fun addLaggingIndicator(indicatorName: String) = ioRun {
        if (indicatorName.trim().isBlank()) return@ioRun

        // Update UI States
        _uiState.update {
            val newList = it.laggingIndicators.toMutableList()
            newList.add(Pair(Uuid.random().toString(), indicatorName))
            it.copy(laggingIndicators = newList)
        }
    }

    fun updateLaggingIndicator(indicatorId: String, newName: String) = ioRun {
        if (newName.trim().isBlank()) return@ioRun

        // Update UI States
        _uiState.update { state ->
            val newList = state.laggingIndicators.map {
                if (it.first == indicatorId) {
                    Pair(
                        it.first,
                        newName,
                    )
                } else it
            }

            state.copy(laggingIndicators = newList)
        }
    }

    fun deleteLaggingIndicator(indicatorId: String) = ioRun {
        // Update UI States
        _uiState.update { state ->
            val newList = state.laggingIndicators.filterNot { it.first == indicatorId }
            state.copy(laggingIndicators = newList)
        }
    }

    fun updateReward(reward: String) {
        _uiState.update { it.copy(reward = reward) }
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

    fun onChangedReview(review: String) {
        _uiState.update { it.copy(review = review) }
    }

    fun onChangedStars(stars: Int) {
        _uiState.update { it.copy(stars = stars) }
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
        _uiState.update {
            it.copy(
                coverUri = yearEntity.cover,
                name = yearEntity.name,
                laggingIndicators = initialLaggingIndicators,
                reward = yearEntity.reward,
                rewardImages = yearEntity.rewardImages,
                review = yearEntity.review,
                stars = yearEntity.stars,
            )
        }
    }

    fun updateYear(onUpdateSuccess: () -> Unit) = ioRun {
        if (!validateInputDate()) return@ioRun
        globalLoading.setLoading(true, "Updating Year")

        val accountId = appStorage.read(CURRENT_ACCOUNT_ID_KEY, "")
        if (accountId.isBlank()) {
            showToast("Failed to find current account. Please try again.")
            return@ioRun
        }

        val directory = application.applicationContext.filesDir
        val folder = "${directory.absolutePath}/AC-$accountId/YE-${yearEntity.id}"

        // Update cover and reward images if needed
        checkAndUpdateCoverAndRewardImages(folder)

        val updateLaggingIndicatorsResult = updateLaggingIndicators(yearEntity.id)
        if (!updateLaggingIndicatorsResult) return@ioRun

        val updateYearResult = database.updateTwelveWeekYear(
            yearToUpdate.copy(
                name = uiState.value.name,
                reward = uiState.value.reward,
                review = uiState.value.review,
                stars = uiState.value.stars,
                address = uiState.value.address,
                longitude = uiState.value.longitude,
                latitude = uiState.value.latitude,
                updatedAt = System.currentTimeMillis(),
            )
        )

        if (updateYearResult == -1) {
            showToast("Failed to update year. Please try again.")
            return@ioRun
        }

        globalLoading.setLoading(false)
        showToast("Year updated successfully!")
        delay(500L)
        onUpdateSuccess()
    }

    // ================================ PRIVATE FUNCTIONS ================================
    private suspend fun updateLaggingIndicators(yearId: String): Boolean {
        val currentIndicatorIds = uiState.value.laggingIndicators.map { it.first }.toSet()
        val initialIndicatorIds = initialLaggingIndicators.map { it.first }.toSet()

        val indicatorsToDelete = initialLaggingIndicators
            .filterNot { currentIndicatorIds.contains(it.first) }
            .map { it.first }

        if (indicatorsToDelete.isNotEmpty()) {
            val deleteResult = database.deleteMultiLaggingIndicators(indicatorsToDelete)
            if (deleteResult != indicatorsToDelete.size) {
                showToast("Failed to delete removed lagging indicators. Please try again.")
                return false
            }
        }

        val indicatorsToAdd = uiState
            .value
            .laggingIndicators
            .filterNot { initialIndicatorIds.contains(it.first) }

        if (indicatorsToAdd.isNotEmpty()) {
            val addResults = database.addAllLaggingIndicators(indicatorsToAdd.map {
                LaggingIndicatorEntity(
                    yearId = yearId,
                    name = it.second,
                )
            })

            if (addResults.contains(-1)) {
                showToast("Failed to add new lagging indicators. Please try again.")
                return false
            }
        }

        return true
    }

    private suspend fun checkAndUpdateCoverAndRewardImages(folder: String) = coroutineScope {
        val newCoverDeferred = async {
            val currentCover = uiState.value.coverUri
            if (currentCover == yearEntity.cover) return@async yearEntity.cover

            val coverPath = "$folder/COVER-${Uuid.random()}.jpg"
            val newUri = InternalStorageFuncs.copyImageToInternalStorageApp(
                context = application.applicationContext,
                uri = currentCover!!,
                path = coverPath,
            )

            if (newUri.isBlank()) {
                showToast("Failed to save new cover image. Please try again.")
                return@async yearEntity.cover
            }

            val deleted = InternalStorageFuncs.deleteFileFromInternalStorage(yearEntity.cover)
            if (!deleted) {
                showToast("Failed to delete old cover image. Please try again.")
                return@async yearEntity.cover
            }

            newUri
        }

        val newRewardImagesDeferred = async {
            val newRewardImages = uiState.value.rewardImages
            val oldRewardImages = yearEntity.rewardImages

            if (newRewardImages.toSet() == oldRewardImages.toSet()) return@async oldRewardImages

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

            oldRewardImages.filterNot { imagesToDelete.contains(it) } + copiedImages
        }

        val resolvedCover = newCoverDeferred.await()
        val resolvedRewardImages = newRewardImagesDeferred.await()
        yearToUpdate = yearToUpdate.copy(
            cover = resolvedCover,
            rewardImages = resolvedRewardImages,
        )
    }

    private suspend fun validateInputDate(): Boolean {
        if (uiState.value.coverUri == null) {
            showToast("Please select a cover image.")
            return false
        }

        if (uiState.value.name.trim().isBlank()) {
            showToast("Please enter a name for the year.")
            return false
        }

        if (uiState.value.startTimeMilliseconds >= uiState.value.endTimeMilliseconds) {
            showToast("Start time must be before end time.")
            return false
        }

        val startDate = Instant.fromEpochMilliseconds(uiState.value.startTimeMilliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate = Instant.fromEpochMilliseconds(uiState.value.endTimeMilliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDateAfter12Week = startDate.plus(12, DateTimeUnit.WEEK)

        if (endDate != startDateAfter12Week) {
            showToast("The end date must be exactly 12 weeks after the start date.")
            return false
        }

        if (uiState.value.laggingIndicators.isEmpty()) {
            showToast("Please add at least one lagging indicator.")
            return false
        }

        // Max lagging indicators is 5
        if (uiState.value.laggingIndicators.size > 5) {
            showToast("You can add up to 5 lagging indicators.")
            return false
        }

        if (uiState.value.reward.trim().isBlank()) {
            showToast("Please enter a reward.")
            return false
        }

        // Max reward images is 5
        if (uiState.value.rewardImages.size > 5) {
            showToast("You can add up to 5 reward images.")
            return false
        }

        return true
    }
}