package com.flosun.pomodoro.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R

val fontFamily = FontFamily(
    Font(R.font.andika_regular, weight = FontWeight.Normal),
    Font(R.font.andika_bold, weight = FontWeight.Bold),
)

val poetsenoneFontFamily = FontFamily(
    Font(R.font.poetsenone_regular, weight = FontWeight.Normal),
)

val firasansFontFamily = FontFamily(
    Font(R.font.firasans_black, weight = FontWeight.Black),
)


@Immutable
data class AppTypographies(
    val body: TextStyle,
    val label: TextStyle,
    val heading: TextStyle,
    val clock: TextStyle,
)

val Typography = AppTypographies(
    body = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
    ),
    label = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
    ),
    heading = TextStyle(
        fontFamily = fontFamily,
        fontSize = 18.sp,
    ),
    clock = TextStyle(
        fontFamily = firasansFontFamily,
        fontSize = 48.sp,
    )
)

inline val LocalTypography
    get() = staticCompositionLocalOf { Typography }
