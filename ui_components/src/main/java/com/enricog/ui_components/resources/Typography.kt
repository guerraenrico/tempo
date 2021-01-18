package com.enricog.ui_components.resources

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
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
        fontSize = 22.sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 14.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 12.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 10.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
)