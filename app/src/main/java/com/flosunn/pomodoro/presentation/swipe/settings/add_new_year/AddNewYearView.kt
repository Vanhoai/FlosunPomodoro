package com.flosunn.pomodoro.presentation.swipe.settings.add_new_year

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreTextField
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.TwoOptionActions
import com.flosunn.pomodoro.ui.theme.AppTheme

data class LaggingIndicator(
    val name: String,
)

@Composable
fun AddNewYearView(navBackStack: NavBackStack<NavKey>) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Component States
    var reward by remember { mutableStateOf("") }
    var coverUri by remember { mutableStateOf<String?>(null) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                coverUri = uri.toString()
            } else {
                Toast.makeText(context, "No media selected", Toast.LENGTH_SHORT).show()
            }
        }

    var laggingIndicators by remember {
        mutableStateOf(
            listOf(
                LaggingIndicator("Build Flosun Studio"),
                LaggingIndicator("Fluent in English and Chinese, make sure to practice every day"),
                LaggingIndicator("Read 12 books"),
                LaggingIndicator("Run at pace of 5 min/km for 10km"),
            )
        )
    }

    Scaffold(containerColor = AppTheme.colors.backgroundColor) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            item {
                CommonBackHeading(
                    onBack = { navBackStack.removeLastOrNull() },
                    title = "Add New Year",
                )
            }

            item {
                UploadCover(
                    coverUri = coverUri,
                    onUpload = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )
            }

            item {
                Text(
                    text = "Duration",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(140.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "12/23/2026",
                            fontSize = 16.sp,
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .height(1.dp)
                            .weight(1f),
                        color = Color(0xFFCCCCCC)
                    )

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(140.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFCCCCCC),
                                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "12/23/2026",
                            fontSize = 16.sp,
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 12.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Lagging Indicators",
                        fontSize = 18.sp,
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color(0xFF5F5F5F),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    laggingIndicators.forEachIndexed { index, indicator ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_tag),
                                contentDescription = null,
                                tint = Color(0xFF5F5F5F),
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .size(20.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                            ) {
                                Text(
                                    text = indicator.name,
                                    fontSize = 16.sp,
                                    modifier = Modifier,
                                )
                            }

                            Icon(
                                painter = painterResource(R.drawable.ic_vertical_dots),
                                contentDescription = null,
                                tint = Color(0xFF7F7F7F),
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .size(20.dp)
                            )
                        }

                        if (index != laggingIndicators.size - 1) HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color(0xFFCCCCCC)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Reward",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
                )

                CoreTextField(
                    value = reward,
                    onValueChanged = { reward = it },
                    maxLines = 4,
                    singleLine = false,
                    height = 100.dp,
                    placeholder = "e.g., Go to travel, buy a new phone, etc.",
                )
            }

            item {
                TwoOptionActions(modifier = Modifier.padding(20.dp))
            }
        }
    }
}