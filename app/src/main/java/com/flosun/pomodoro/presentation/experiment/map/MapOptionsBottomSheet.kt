package com.flosun.pomodoro.presentation.experiment.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.shared.LabelOptions
import com.flosun.pomodoro.ui.components.shared.SelectBoxMenuOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapOptionsBottomSheet(
    state: MapState,
    isExpanded: Boolean = false,
    onClose: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val mapStyles = listOf(
        Protomaps.entries,
        OpenFreeMap.entries,
        Versatiles.entries,
        OtherStyles.entries,
    ).flatten()

    if (isExpanded) CoreBottomSheet(
        title = "Map Options",
        modifier = Modifier,
        closeBottomSheet = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                onClose()
            }
        },
        sheetState = sheetState,
    ) {
        SelectMapStyle(
            styles = mapStyles,
            selectedMapStyle = state.selectedStyle,
            onChangedSelectedStyle = { state.selectedStyle = it },
        )

        Box(modifier = Modifier.height(400.dp))

    }

}

fun splitLastClassName(className: String): String {
    val parts = className.split(".")
    return if (parts.isNotEmpty()) parts.last() else className
}

@Composable
fun SelectMapStyle(
    selectedMapStyle: MapStyle? = null,
    styles: List<MapStyle> = emptyList(),
    onChangedSelectedStyle: (MapStyle) -> Unit = {},
) {
    SelectBoxMenuOption(
        title = "Map Style",
        selectedOption = selectedMapStyle,
        entities = styles,
        onChanged = onChangedSelectedStyle,
        labelOptions = object : LabelOptions<MapStyle> {
            override fun selectBoxLabel(entity: MapStyle): String {
                return "${splitLastClassName(entity.javaClass.name)} ${entity.displayName}"
            }

            override fun menuLabel(entity: MapStyle): String {
                return "${splitLastClassName(entity.javaClass.name)} ${entity.displayName}"
            }

            override fun noChoiceLabel(): String {
                return "Select Map Style"
            }
        }
    )
}