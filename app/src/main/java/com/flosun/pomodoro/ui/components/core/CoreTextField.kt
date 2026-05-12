package com.flosun.pomodoro.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.theme.AppTheme
import timber.log.Timber

@Composable
fun CoreTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = remember { FocusRequester() },
    value: String,
    onValueChanged: (String) -> Unit,
    height: Dp = 52.dp,
    backgroundColor: Color = Color.White,
    isEnabled: Boolean = true,
    placeholder: String = "e.g. Build Flosun Studio",
    padding: PaddingValues = PaddingValues(),
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    validate: ((String) -> Boolean)? = null,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isValid = validate?.invoke(value) ?: false

    LaunchedEffect(isFocused) { onFocusChanged?.invoke(isFocused) }

    Row(
        modifier = modifier
            .padding(padding)
            .fillMaxWidth()
            .height(height)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .border(
                width = 1.dp,
                color = AppTheme.colors.borderColor,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .pointerInput(Unit) { detectTapGestures {} },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefix != null) Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 12.dp)
                .fillMaxHeight(),
            contentAlignment = if (singleLine) Alignment.Center else Alignment.TopStart
        ) {
            prefix()
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            BasicTextField(
                value = value,
                onValueChange = onValueChanged,
                maxLines = maxLines,
                visualTransformation = visualTransformation,
                enabled = isEnabled,
                singleLine = singleLine,
                interactionSource = interactionSource,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                    ) {
                        innerTextField()
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 16.sp,
                                color = Color.LightGray,
                                modifier = Modifier,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                },
                textStyle = AppTheme.typography.body,
                cursorBrush = Brush.verticalGradient(
                    listOf(
                        AppTheme.colors.primaryColor,
                        AppTheme.colors.primaryColor
                    )
                ),
            )
        }

        if (suffix != null) Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(end = 20.dp),
            contentAlignment = if (singleLine) Alignment.Center else Alignment.TopEnd
        ) {
            suffix()
        }
    }
}