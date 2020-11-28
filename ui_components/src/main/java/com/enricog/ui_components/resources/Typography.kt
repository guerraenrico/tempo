package com.enricog.ui_components.resources

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import com.guerrae.ui_components.R

private val Prompt = fontFamily(
    font(R.font.prompt_light, FontWeight.Light),
    font(R.font.prompt, FontWeight.Normal),
    font(R.font.prompt_bold, FontWeight.Bold)
)

data class Typography(
    val title: TextStyle
)

internal val defaultTypography = Typography(
    title = TextStyle(
        fontFamily = Prompt,
        fontWeight = FontWeight.Bold
    )
)