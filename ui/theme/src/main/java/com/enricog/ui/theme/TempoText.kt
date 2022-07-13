package com.enricog.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LocalTempoTextFieldStyle = staticCompositionLocalOf {
    TextStyle(
        fontFamily = FontFamilyDefault,
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 20.sp
    )
}
