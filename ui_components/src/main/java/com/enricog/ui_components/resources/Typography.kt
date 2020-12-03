package com.enricog.ui_components.resources

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.R

internal val Prompt = fontFamily(
    font(resId = R.font.prompt_light, weight = FontWeight.Light),
    font(resId = R.font.prompt, weight = FontWeight.Normal),
    font(resId = R.font.prompt_bold, weight = FontWeight.Bold)
)

internal val defaultTypography = Typography(
    defaultFontFamily = Prompt,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    )
)