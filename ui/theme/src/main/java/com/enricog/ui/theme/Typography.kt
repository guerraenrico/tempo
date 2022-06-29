package com.enricog.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val FontFamilyDefault = FontFamily(
    Font(resId = R.font.prompt_light, weight = FontWeight.Light),
    Font(resId = R.font.prompt_normal, weight = FontWeight.Normal),
    Font(resId = R.font.prompt_bold, weight = FontWeight.Bold)
)

internal val FontFamilyMono = FontFamily(
    Font(R.font.jetbrains_mono_extra_bold, weight = FontWeight.ExtraBold)
)

@Immutable
object TempoTypography {

    val defaultFontFamily: FontFamily = FontFamilyDefault
    val defaultFontFamilyMono: FontFamily = FontFamilyMono

    val h1: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 22.sp
    )
    val h2: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
    val h3: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    )
    val h4: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 14.sp
    )
    val h5: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 12.sp
    )
    val h6: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 10.sp
    )
    val body1: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 18.sp
    )
    val body2: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 16.sp
    )
    val button: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 12.sp
    )
}

internal val LocalTempoTypography = staticCompositionLocalOf { TempoTypography }

internal val defaultTypography = Typography(
    defaultFontFamily = TempoTypography.defaultFontFamily,
    h1 = TempoTypography.h1,
    h2 = TempoTypography.h2,
    h3 = TempoTypography.h3,
    h4 = TempoTypography.h4,
    h5 = TempoTypography.h5,
    h6 = TempoTypography.h6,
    body1 = TempoTypography.body1,
    body2 = TempoTypography.body2,
    button = TempoTypography.button,
    caption = TempoTypography.caption
)
