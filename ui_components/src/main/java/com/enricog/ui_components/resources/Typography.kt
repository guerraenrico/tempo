package com.enricog.ui_components.resources

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.R

private val Prompt = fontFamily(
    font(R.font.prompt_light, FontWeight.Light),
    font(R.font.prompt, FontWeight.Normal),
    font(R.font.prompt_bold, FontWeight.Bold)
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