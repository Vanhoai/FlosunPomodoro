package com.flosun.pomodoro.presentation.swipe.tasks.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.launch


val SLIDE_SPACING = 2.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OptionsSlider(
    selectedOptionIndex: Int = 0,
    options: List<String> = emptyList(),
    onChangedOptionIndex: (Int) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val containerWidth = screenWidth - 40.dp
    val optionWidth = (containerWidth - SLIDE_SPACING * (options.size + 1)) / options.size

    var selectedIndexInside by remember { mutableIntStateOf(selectedOptionIndex) }

    // Two-way syncing of selected index to make sure the animation works when the index is changed from outside
    LaunchedEffect(selectedOptionIndex) {
        selectedIndexInside = selectedOptionIndex
    }

    val offset = animateDpAsState(
        targetValue = (optionWidth + SLIDE_SPACING) * selectedIndexInside + SLIDE_SPACING,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "SliderOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(40.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(optionWidth)
                .height(36.dp)
                .offset(x = offset.value)
                .background(
                    color = AppTheme.colors.primaryColor,
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                ),
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .width(optionWidth)
                        .height(40.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                scope.launch { selectedIndexInside = index }
                                scope.launch { onChangedOptionIndex(index) }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option,
                        color = if (selectedIndexInside == index) Color.White else Color(
                            0xFF636363
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}