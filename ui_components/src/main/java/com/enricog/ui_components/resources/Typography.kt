package com.enricog.ui_components.resources

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.R

val Prompt = fontFamily(
    font(resId = R.font.prompt_light, weight = FontWeight.Light),
    font(resId = R.font.prompt, weight = FontWeight.Normal),
    font(resId = R.font.prompt_bold, weight = FontWeight.Bold)
)

@Stable
data class TempoTypography(
    val title: TextStyle
)

internal val defaultTypography = TempoTypography(
    title = TextStyle(
        fontFamily = Prompt,
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    )
)

internal val AmbientTypography = staticAmbientOf { defaultTypography }