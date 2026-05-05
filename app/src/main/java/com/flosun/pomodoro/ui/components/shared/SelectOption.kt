package com.flosun.pomodoro.ui.components.shared

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.cropVertical
import com.flosunn.core.extensions.rippleEffectClickable

interface LabelOptions<T> {
    fun selectBoxLabel(entity: T): String
    fun menuLabel(entity: T): String
    fun noChoiceLabel(): String
}

@Composable
fun <T> SelectBoxMenuOption(
    title: String,
    selectedOption: T? = null,
    entities: List<T> = emptyList(),
    onChanged: (T) -> Unit = {},
    labelOptions: LabelOptions<T>,
) {
    val context = LocalContext.current
    val screenWidth = ViewFuncs.screenWidthDp()
    var expandedSelectWeek by remember { mutableStateOf(false) }

    val label = selectedOption?.let {
        labelOptions.selectBoxLabel(selectedOption)
    } ?: labelOptions.noChoiceLabel()

    Text(
        text = title,
        fontSize = 18.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 8.dp),
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(8.dp),
            )
            .rippleEffectClickable {
                if (entities.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please create a ${title.lowercase()} first.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@rippleEffectClickable
                }

                expandedSelectWeek = true
            }
            .padding(12.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = Color(0xFF141B34),
            modifier = Modifier.size(16.dp),
        )

        DropdownMenu(
            expanded = expandedSelectWeek,
            onDismissRequest = { expandedSelectWeek = false },
            containerColor = Color.White,
            modifier = Modifier
                .width(screenWidth.dp - 40.dp)
                .cropVertical(8.dp),
            offset = DpOffset(x = 10.dp, y = 0.dp),
        ) {
            entities.forEach { entity ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(
                            text = labelOptions.menuLabel(entity),
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = AppTheme.typography.body
                        )
                    },
                    onClick = {
                        onChanged(entity)
                        expandedSelectWeek = false
                    }
                )
            }
        }
    }
}