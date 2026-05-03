package com.flosun.pomodoro.presentation.week_year.add_new_year

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.flosun.pomodoro.adapters.database.entities.TwelveWeekYearEntity
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_ID_KEY
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.utils.AppStorage
import com.flosun.pomodoro.core.utils.StringArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import androidx.core.net.toUri
import com.flosun.pomodoro.adapters.database.PomodoroDatabase
import com.flosun.pomodoro.adapters.database.entities.LaggingIndicatorEntity
import com.flosun.pomodoro.adapters.database.entities.WeekEntity
import com.flosun.pomodoro.core.functions.TimeFuncs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltViewModel
class AddNewYearViewModel @Inject constructor(
    private val application: Application,
    private val appStorage: AppStorage,
    private val database: PomodoroDatabase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddNewYearUIState())
    val uiState: StateFlow<AddNewYearUIState> = _uiState.asStateFlow()

    init {
        val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val endTime = startTime.plus(12, DateTimeUnit.WEEK)

        val startTimeMilliseconds = startTime
            .atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        val endTimeMilliseconds = endTime
            .atTime(23, 59, 59)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        _uiState.update {
            it.copy(
                startTimeMilliseconds = startTimeMilliseconds,
                endTimeMilliseconds = endTimeMilliseconds
            )
        }
    }

    fun updateCoverUri(uri: String) {
        _uiState.update { it.copy(coverUri = uri) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateStartTime(startTime: Long) {
        // also update end time to be 12 weeks after start time
        val startDate = Instant.fromEpochMilliseconds(startTime)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endTimeMilliseconds = startDate.plus(12, DateTimeUnit.WEEK)
            .atTime(23, 59, 59)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        _uiState.update {
            it.copy(
                startTimeMilliseconds = startTime,
                endTimeMilliseconds = endTimeMilliseconds,
            )
        }
    }

    fun updateEndTime(endTime: Long) {
        _uiState.update { it.copy(endTimeMilliseconds = endTime) }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addLaggingIndicator(name: String) {
        val newLaggingIndicator = Pair(Uuid.random().toString(), name)
        _uiState.update { it.copy(laggingIndicators = it.laggingIndicators + newLaggingIndicator) }
    }

    fun deleteLaggingIndicator(indicatorUuid: String) {
        val newLaggingIndicators = _uiState.value.laggingIndicators
            .filterNot { it.first == indicatorUuid }
        _uiState.update { it.copy(laggingIndicators = newLaggingIndicators) }
    }

    fun updateLaggingIndicator(uuid: String, name: String) {
        val newLaggingIndicators = _uiState.value.laggingIndicators.map {
            if (it.first == uuid) Pair(it.first, name) else it
        }
        _uiState.update { it.copy(laggingIndicators = newLaggingIndicators) }
    }

    fun updateReward(reward: String) {
        _uiState.update { it.copy(reward = reward) }
    }

    fun updateRewardImages(newImages: List<String>) {
        _uiState.update { it.copy(rewardImages = newImages) }
    }

    fun deleteRewardImage(imageUrl: String) {
        val newImages = _uiState.value.rewardImages.filterNot { it == imageUrl }
        _uiState.update { it.copy(rewardImages = newImages) }
    }

    fun resetState() {
        val startTime = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val endTime = startTime.plus(12, DateTimeUnit.WEEK)

        val startTimeMilliseconds = startTime
            .atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        val endTimeMilliseconds = endTime
            .atTime(23, 59, 59)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        _uiState.value = AddNewYearUIState(
            name = "",
            coverUri = null,
            reward = "",
            rewardImages = emptyList(),
            laggingIndicators = emptyList(),
            startTimeMilliseconds = startTimeMilliseconds,
            endTimeMilliseconds = endTimeMilliseconds,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addNewYear(
        onAddSuccess: () -> Unit = {},
        onAddFailure: () -> Unit = {},
    ) = viewModelScope.launch(Dispatchers.IO) {
        // Validate input data
        if (!validateInputDate()) return@launch
        // Check if the duration overlaps with other years
        val isOverlap = checkDurationOverlapOthersYears(
            startTimeMilliseconds = uiState.value.startTimeMilliseconds,
            endTimeMilliseconds = uiState.value.endTimeMilliseconds,
        )

        if (isOverlap) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "The duration overlaps with another year. Please adjust the start and end time.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        // Find Current AccountId
        val accountId = appStorage.read(CURRENT_ACCOUNT_ID_KEY, "")
        if (accountId.isBlank()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to find current account. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        val directory = application.applicationContext.filesDir
        val yearId = Uuid.random().toString()
        val folder = "${directory.absolutePath}/AC-$accountId/YE-$yearId"

        val (coverUri, rewardImageUris) = coroutineScope {
            // copy cover image
            val coverPath = "$folder/COVER-${Uuid.random()}.jpg"
            val coverDeferred = async {
                copyImageToInternalStorageApp(
                    uri = uiState.value.coverUri!!,
                    path = coverPath,
                )
            }

            // copy reward images
            val rewardImagesDeferred = uiState.value.rewardImages.mapIndexed { index, string ->
                val rewardImagePath = "$folder/REWARD-${index}-${Uuid.random()}.jpg"
                async {
                    copyImageToInternalStorageApp(
                        uri = string,
                        path = rewardImagePath,
                    )
                }
            }

            val cover = coverDeferred.await()
            val rewards = rewardImagesDeferred.awaitAll()

            cover to rewards
        }

        if (coverUri.isBlank() || rewardImageUris.any() { it.isBlank() }) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to copy images to internal storage. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        val newYear = TwelveWeekYearEntity(
            id = yearId,
            accountId = accountId,
            name = uiState.value.name,
            cover = coverUri,
            reward = uiState.value.reward,
            rewardImages = rewardImageUris,
            startTimeMilliseconds = uiState.value.startTimeMilliseconds,
            endTimeMilliseconds = uiState.value.endTimeMilliseconds,
        )

        val (newYearResult, weekResult, indicatorResults) = coroutineScope {
            val newYear = async { database.addNewTwelveWeekYear(newYear) }
            val weeks = async { addAllWeeks(yearId, uiState.value.startTimeMilliseconds) }
            val laggingIndicators = async { addAllLaggingIndicators(yearId) }

            Triple(newYear.await(), weeks.await(), laggingIndicators.await())
        }

        if (newYearResult <= 0 || !weekResult || !indicatorResults) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to add new year to database. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return@launch
        }

        onAddSuccess()
    }

    // region ==================================== PRIVATE FUNCTIONS ====================================
    private suspend fun addAllWeeks(yearId: String, startTimeMilliseconds: Long): Boolean {
        val startTimeWeek = TimeFuncs.dateTimeFromMilliseconds(startTimeMilliseconds).date
        val startTimeWeekMillis = startTimeWeek.atTime(0, 0, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        val weekPeriod = 7 * 24 * 60 * 60 * 1000L

        var startTime = startTimeWeekMillis
        val weeks = mutableListOf<WeekEntity>()
        for (i in 1..12) {
            val endTime = startTime + weekPeriod - 1
            val week = WeekEntity(
                yearId = yearId,
                name = "Week $i",
                startTimeMilliseconds = startTime,
                endTimeMilliseconds = endTime,
            )

            weeks.add(week)
            startTime += weekPeriod // Move to the next week
        }

        val result = database.addAllWeeks(weeks)
        Timber.tag(DEBUG_TAG).d("Add all weeks result: $result")
        if (result.any { it <= 0 }) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to add weeks to database. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        return true
    }

    private suspend fun addAllLaggingIndicators(yearId: String): Boolean {
        val laggingIndicators = uiState.value.laggingIndicators.map {
            LaggingIndicatorEntity(
                yearId = yearId,
                name = it.second,
            )
        }

        val result = database.addAllLaggingIndicators(laggingIndicators)
        Timber.tag(DEBUG_TAG).d("Add all lagging indicators result: $result")
        if (result.any { it <= 0 }) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Failed to add lagging indicators to database. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return result.all { it > 0 }
    }

    private fun copyImageToInternalStorageApp(
        uri: String,
        path: String,
    ): String {
        try {
            val contentResolver = application.contentResolver
            val inputStream = contentResolver.openInputStream(uri.toUri())
                ?: throw Exception("Failed to open input stream from URI: $uri")

            val file = java.io.File(path).apply { parentFile?.mkdirs() }
            inputStream.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            return path
        } catch (exception: Exception) {
            exception.printStackTrace()
            Toast.makeText(
                application.applicationContext,
                "Failed to copy image to internal storage. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
            return ""
        }
    }

    private suspend fun validateInputDate(): Boolean {
        if (uiState.value.coverUri == null) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Please select a cover image.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return false
        }

        if (uiState.value.name.trim().isBlank()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Please enter a name for the year.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        if (uiState.value.startTimeMilliseconds >= uiState.value.endTimeMilliseconds) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Start time must be before end time.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        val startDate = Instant.fromEpochMilliseconds(uiState.value.startTimeMilliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate = Instant.fromEpochMilliseconds(uiState.value.endTimeMilliseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDateAfter12Week = startDate.plus(12, DateTimeUnit.WEEK)

        if (endDate != startDateAfter12Week) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "The end date must be exactly 12 weeks after the start date.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        if (uiState.value.laggingIndicators.isEmpty()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Please add at least one lagging indicator.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        // Max lagging indicators is 5
        if (uiState.value.laggingIndicators.size > 5) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "You can add up to 5 lagging indicators.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        if (uiState.value.reward.trim().isBlank()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "Please enter a reward.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        // Max reward images is 5
        if (uiState.value.rewardImages.size > 5) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    application.applicationContext,
                    "You can add up to 5 reward images.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return false
        }

        return true
    }

    private suspend fun checkDurationOverlapOthersYears(
        startTimeMilliseconds: Long,
        endTimeMilliseconds: Long,
    ): Boolean {
        val years = database.findAllTwelveWeekYears().first()
        return years.any {
            val isOverlap = TimeFuncs.isTimeOverlap(
                startTime1 = startTimeMilliseconds,
                endTime1 = endTimeMilliseconds,
                startTime2 = it.startTimeMilliseconds,
                endTime2 = it.endTimeMilliseconds,
            )

            isOverlap
        }
    }
    // endregion ==================================== PRIVATE FUNCTIONS ====================================
}