package com.flosunn.pomodoro.ui.components.core.datepicker

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.pomodoro.ui.components.core.AnimatedFadeVisibility
import com.flosunn.pomodoro.ui.theme.AppTheme
import timber.log.Timber

data class DatePickerData(
    val year: Int,
    val month: Int,
    val day: Int,
)

object DefaultDatePickerData {
    private val calendar = Calendar.getInstance()

    val defaultDate = DatePickerData(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )
}

// Constants
private val HEADER_HEIGHT = 32.dp
private val months = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)
private val years = (2000..2026).toList().map { it.toString() }

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit,
    date: DatePickerData = DefaultDatePickerData.defaultDate,
    days: List<String>? = null,
    month: List<String>? = null,
    viewModel: DatePickerViewModel = hiltViewModel<DatePickerViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Card(
        shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            DatePickerHeader(
                height = HEADER_HEIGHT,
                title = "May 2026",
                isMonthYearViewVisible = uiState.isMonthYearViewVisible,
                onPrevious = {},
                onNext = {},
                onPressMonthYear = { viewModel.toggleMonthYearView() },
                onOk = { viewModel.toggleMonthYearView() },
            )

            AnimatedFadeVisibility(uiState.isMonthYearViewVisible) {
                WheelMonthYearPicker(
                    selectedMonthIndex = uiState.selectedMonthIndex,
                    selectedYearIndex = uiState.selectedYearIndex,
                    onSelectedMonthIndex = { viewModel.updateSelectedMonthIndex(it) },
                    onSelectedYearIndex = { viewModel.updateSelectedYearIndex(it) },
                    years = uiState.years,
                    months = uiState.months,
                    modifier = Modifier.padding(top = HEADER_HEIGHT),
                )
            }
        }
    }
}

val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

@Composable
fun DateView(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = modifier,
    ) {
        items(days) { day ->
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = day,
                    color = Color.Black,
                    fontSize = 14.sp,
                )
            }
        }
    }
}


