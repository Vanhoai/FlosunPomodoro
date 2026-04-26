package com.flosunn.core.libraries.datepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flosunn.core.libraries.datepicker.ui.DatePickerHeading
import com.flosunn.core.libraries.datepicker.ui.DateView
import com.flosunn.core.libraries.datepicker.ui.WheelMonthYearPicker
import com.flosunn.core.ui.AnimatedFadeVisibility
import kotlinx.datetime.number

private val HEADER_HEIGHT = 32.dp

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    onDateSelected: (Int, Int, Int) -> Unit,
    days: List<String>? = null,
    month: List<String>? = null,
    viewModel: DatePickerViewModel = hiltViewModel<DatePickerViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            DatePickerHeading(
                height = HEADER_HEIGHT,
                title = viewModel.getMonthYear(),
                isMonthYearViewVisible = uiState.isMonthYearViewVisible,
                onPrevious = { viewModel.previousMonth() },
                onNext = { viewModel.nextMonth() },
                onPressMonthYear = { viewModel.toggleMonthYearView() },
            )

            AnimatedFadeVisibility(!uiState.isMonthYearViewVisible) {
                DateView(
                    selectedDayOfMonth = uiState.selectedDayOfMonth,
                    onDaySelected = {
                        viewModel.updateSelectedDayOfMonth(it)
                        val date = uiState.date
                        onDateSelected(date.year, date.month.number, it)
                    },
                    days = viewModel.daysInGrid(),
                    dayNames = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = HEADER_HEIGHT),
                )
            }

            AnimatedFadeVisibility(uiState.isMonthYearViewVisible) {
                WheelMonthYearPicker(
                    selectedMonthIndex = uiState.selectedMonthIndex,
                    selectedYearIndex = uiState.selectedYearIndex,
                    onSelectedMonthIndex = { viewModel.updateSelectedMonthIndex(it) },
                    onSelectedYearIndex = { viewModel.updateSelectedYearIndex(it) },
                    years = viewModel.years(),
                    months = viewModel.months(),
                    modifier = Modifier.padding(top = HEADER_HEIGHT),
                )
            }
        }
    }
}
