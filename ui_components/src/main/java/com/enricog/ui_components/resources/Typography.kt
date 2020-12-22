package com.enricog.ui_components.resources

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.R

internal val FontFamilyDefault = fontFamily(
    font(resId = R.font.prompt_light, weight = FontWeight.Light),
    font(resId = R.font.prompt_normal, weight = FontWeight.Normal),
    font(resId = R.font.prompt_bold, weight = FontWeight.Bold)
)

val FontFamilyMono = fontFamily(
    font(R.font.jetbrains_mono_extra_bold, weight = FontWeight.ExtraBold)
)

internal val defaultTypography = Typography(
    defaultFontFamily = FontFamilyDefault,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
)